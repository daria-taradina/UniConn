// Loads another user's profile page (/profile/{username}).
// Delegates post card rendering to postCardRenderer.js.

(function () {
  const token = localStorage.getItem('token');
  const authHeaders = token ? { 'Authorization': 'Bearer ' + token } : {};
  const viewedUsername = window.location.pathname.split('/').filter(Boolean).pop();
  const currentUsername = localStorage.getItem('currentUsername') || '';

  if (viewedUsername && viewedUsername === currentUsername) {
    window.location.replace('/profile');
    return;
  }

  function openPendingPostModal() {
    const raw = sessionStorage.getItem('pendingPostModal');
    if (!raw) return;
    sessionStorage.removeItem('pendingPostModal');
    try {
      const post = JSON.parse(raw);
      if (typeof openPostViewModal === 'function') openPostViewModal(post);
    } catch {}
  }

  function renderTrending(tags) {
    const trendingList = document.getElementById('trending-tags-list');
    if (!trendingList) return;
    if (!tags || tags.length === 0) {
      trendingList.innerHTML = '<li class="trending-tag-empty">No trending topics yet.</li>';
      return;
    }
    trendingList.innerHTML = '';
    tags.forEach((tag, i) => {
      const li = document.createElement('li');
      li.className = 'trending-tag-item';
      li.innerHTML = `<span class="trending-tag-rank">#${i + 1}</span><span class="trending-tag-name">${tag}</span>`;
      li.addEventListener('click', () => openTagPostsModal(tag));
      trendingList.appendChild(li);
    });
  }

  // ── filter dropdown ───────────────────────────────────────────────
  function createFilterBar(allPosts, profilePosts, communityPosts) {
	const bar = document.createElement('div');
	  bar.className = 'posts-filter-bar';          // ← class instead of inline style

	  const select = document.createElement('select');
	  select.className = 'posts-filter-select';    // ← class instead of inline style
	  select.innerHTML = `
	    <option value="all">All posts</option>
	    <option value="profile">Profile posts</option>
	    <option value="community">Community posts</option>
	  `;

    select.addEventListener('change', () => {
      const val = select.value;
      const posts = val === 'all' ? allPosts
                  : val === 'profile' ? profilePosts
                  : communityPosts;
      renderFilteredPosts(posts, bar);
    });

    bar.appendChild(select);
    return bar;
  }

  function renderFilteredPosts(posts, filterBar) {
    const c = document.getElementById('profile-posts-container');
    if (!c) return;
    c.innerHTML = '';
    if (filterBar) c.appendChild(filterBar);
    if (!posts || posts.length === 0) {
      const empty = document.createElement('p');
      empty.style.cssText = 'color:#999;font-size:0.9em;padding:16px';
      empty.textContent = 'No posts yet.';
      c.appendChild(empty);
      return;
    }
    posts.forEach(post => c.appendChild(createPostCard(post)));
  }

  function setupFollowModal(profileUserId) {
    const followModal = document.getElementById('follow-list-modal');
    const followTitle = document.getElementById('follow-list-title');
    const followList  = document.getElementById('follow-list');
    const followClose = document.getElementById('follow-list-close');
    if (!followModal) return;

    function closeFollowModal() {
      followModal.classList.remove('active');
      followModal.setAttribute('aria-hidden', 'true');
    }

    followClose?.addEventListener('click', closeFollowModal);
    followModal.addEventListener('click', e => { if (e.target === followModal) closeFollowModal(); });

    async function openFollowModal(type) {
      followTitle.textContent = type === 'followers' ? 'Followers' : 'Following';
      followList.innerHTML = '<li style="padding:12px 8px;color:#999;font-size:0.9em">Loading...</li>';
      followModal.classList.add('active');
      followModal.setAttribute('aria-hidden', 'false');

      const [users, followingIds] = await Promise.all([
        fetch(`/api/users/${profileUserId}/${type}`, { headers: authHeaders }).then(r => r.ok ? r.json() : []),
        fetch('/api/users/following/ids', { headers: authHeaders }).then(r => r.ok ? r.json() : [])
      ]);

      const followingSet = new Set(followingIds);
      followList.innerHTML = '';

      if (!users.length) {
        followList.innerHTML = `<li style="padding:12px 8px;color:#999;font-size:0.9em">No ${type} yet.</li>`;
        return;
      }

      users.forEach(u => {
        const isSelf = u.username === currentUsername;
        const isFollowing = followingSet.has(u.userId);
        const li = document.createElement('li');
        li.className = 'follow-list-item';
        li.style.cursor = 'pointer';
        li.innerHTML = `
          <img src="${u.profilePicture || '/vector-logos/usernameSignIn.svg'}" alt="" class="follow-list-avatar">
          <div class="follow-list-info">
            <span class="follow-list-username">u/${u.username}</span>
            ${u.name ? `<span class="follow-list-name">${u.name}</span>` : ''}
          </div>
          ${!isSelf ? `<button class="follow-btn ${isFollowing ? 'unfollow-btn' : ''}" data-uid="${u.userId}">${isFollowing ? 'Unfollow' : 'Follow'}</button>` : ''}
        `;
        li.addEventListener('click', e => {
          if (e.target.closest('.follow-btn')) return;
          window.location.href = '/profile/' + u.username;
        });
        if (!isSelf) {
          const btn = li.querySelector('.follow-btn');
          btn.addEventListener('click', async e => {
            e.stopPropagation();
            const following = btn.classList.contains('unfollow-btn');
            const res = await fetch(`/api/users/${u.userId}/${following ? 'unfollow' : 'follow'}`, {
              method: following ? 'DELETE' : 'POST',
              headers: authHeaders
            });
            if (res.ok) {
              btn.textContent = following ? 'Follow' : 'Unfollow';
              btn.classList.toggle('unfollow-btn', !following);
            }
          });
        }
        followList.appendChild(li);
      });
    }

    document.getElementById('stat-followers')?.addEventListener('click', () => openFollowModal('followers'));
    document.getElementById('stat-following')?.addEventListener('click', () => openFollowModal('following'));
  }

  // ── main fetch ────────────────────────────────────────────────────
  Promise.all([
    fetch(`/api/profile/${viewedUsername}`, { headers: authHeaders }).then(r => r.ok ? r.json() : null),
    fetch('/api/community/trending-tags').then(r => r.ok ? r.json() : []),
    token ? fetch('/api/users/following/ids', { headers: authHeaders }).then(r => r.ok ? r.json() : []) : Promise.resolve([])
  ]).then(([profile, trendingTags, followingIds]) => {
    initPostViewModal();
    if (!profile) return;

    // render profile info
    const usernameEl = document.getElementById('profile-username');
    if (usernameEl) usernameEl.textContent = 'u/' + (profile.username || '');

    const fullnameEl = document.getElementById('profile-fullname');
    if (fullnameEl) fullnameEl.textContent = profile.name || '';

    const avatarEl = document.getElementById('profile-picture-img');
    if (avatarEl && profile.profilePicture) avatarEl.src = profile.profilePicture;

    const followerEl  = document.getElementById('follower-count');
    const followingEl = document.getElementById('following-count');
    const communityEl = document.getElementById('community-count');
    if (followerEl)  followerEl.textContent  = profile.followerCount  ?? 0;
    if (followingEl) followingEl.textContent = profile.followingCount ?? 0;
    if (communityEl) communityEl.textContent = profile.communityCount ?? 0;

    const bioSection = document.getElementById('profile-bio-section');
    if (bioSection) {
      bioSection.innerHTML = '';
      const bioEl = document.createElement('p');
      bioEl.className = profile.userBio ? 'profile-bio' : 'profile-bio-empty';
      bioEl.textContent = profile.userBio || 'No bio yet.';
      bioSection.appendChild(bioEl);
    }

    // follow button
    const followBtn = document.getElementById('follow-profile-btn');
    if (followBtn && profile.userId) {
      const isFollowing = new Set(followingIds).has(profile.userId);
      followBtn.textContent = isFollowing ? 'Unfollow' : 'Follow';
      followBtn.className = 'follow-btn' + (isFollowing ? ' unfollow-btn' : '');
      followBtn.style.display = '';
      followBtn.addEventListener('click', async () => {
        const following = followBtn.classList.contains('unfollow-btn');
        const res = await fetch(`/api/users/${profile.userId}/${following ? 'unfollow' : 'follow'}`, {
          method: following ? 'DELETE' : 'POST',
          headers: authHeaders
        });
        if (res.ok) {
          followBtn.textContent = following ? 'Follow' : 'Unfollow';
          followBtn.classList.toggle('unfollow-btn', !following);
          const count = document.getElementById('follower-count');
          if (count) count.textContent = parseInt(count.textContent) + (following ? -1 : 1);
        }
      });
    }

    renderTrending(trendingTags);
    if (profile.userId) setupFollowModal(profile.userId);

    // communities modal
    const commModal      = document.getElementById('communities-modal');
    const commModalList  = document.getElementById('communities-modal-list');
    const commModalClose = document.getElementById('communities-modal-close');
    if (commModal && profile.userId) {
      commModalClose?.addEventListener('click', () => { commModal.classList.remove('active'); commModal.setAttribute('aria-hidden', 'true'); });
      commModal.addEventListener('click', e => { if (e.target === commModal) { commModal.classList.remove('active'); commModal.setAttribute('aria-hidden', 'true'); } });

      document.getElementById('stat-communities')?.addEventListener('click', async () => {
        commModalList.innerHTML = '<li style="padding:12px 8px;color:#999;font-size:0.9em">Loading...</li>';
        commModal.classList.add('active');
        commModal.setAttribute('aria-hidden', 'false');

        const [communities, myCommunities] = await Promise.all([
          fetch(`/api/community/user/${profile.userId}/communities`, { headers: authHeaders }).then(r => r.ok ? r.json() : []),
          fetch('/api/community/my-communities', { headers: authHeaders }).then(r => r.ok ? r.json() : [])
        ]);

        const myIds = new Set(myCommunities.map(c => c.communityId));
        commModalList.innerHTML = '';
        if (!communities.length) {
          commModalList.innerHTML = '<li style="padding:12px 8px;color:#999;font-size:0.9em">No communities yet.</li>';
          return;
        }
        communities.forEach(c => {
          const isMember = myIds.has(c.communityId);
          const isMyAdmin = c.createdByUsername === currentUsername;
          const li = document.createElement('li');
          li.className = 'follow-list-item';
          li.style.cursor = 'pointer';
          li.innerHTML = `
            <img src="${c.communityPicture || '/vector-logos/clubLogo.svg'}" alt="" class="follow-list-avatar">
            <div class="follow-list-info">
              <span class="follow-list-username">c/${c.communityName}</span>
              ${c.description ? `<span class="follow-list-name">${c.description}</span>` : ''}
            </div>
            ${!isMyAdmin ? `<button class="follow-btn ${isMember ? 'unfollow-btn' : ''}">${isMember ? 'Leave' : 'Join'}</button>` : ''}
          `;
          li.addEventListener('click', e => {
            if (e.target.closest('.follow-btn')) return;
            window.location.href = '/community/' + c.communityName;
          });
          if (!isMyAdmin) {
            const btn = li.querySelector('.follow-btn');
            btn.addEventListener('click', async e => {
              e.stopPropagation();
              const leaving = btn.classList.contains('unfollow-btn');
              const res = await fetch(`/api/community/${c.communityId}/${leaving ? 'leave' : 'join'}`, {
                method: leaving ? 'DELETE' : 'POST',
                headers: authHeaders
              });
              if (res.ok) {
                btn.textContent = leaving ? 'Join' : 'Leave';
                btn.classList.toggle('unfollow-btn', !leaving);
              }
            });
          }
          commModalList.appendChild(li);
        });
      });
    }

    // fetch posts for both types then render with filter
    return Promise.all([
      fetch(`/api/posts/profile/by-username/${viewedUsername}`, { headers: authHeaders }).then(r => r.ok ? r.json() : []),
      fetch(`/api/posts/user/${profile.userId}/community`, { headers: authHeaders }).then(r => r.ok ? r.json() : [])
    ]).then(([profilePosts, communityPosts]) => {
      const allPosts = [...profilePosts, ...communityPosts]
        .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      const filterBar = createFilterBar(allPosts, profilePosts, communityPosts);
      renderFilteredPosts(allPosts, filterBar);
      openPendingPostModal();
    });

  }).catch(() => {});

  // ── search filters for follow and communities modals ──────────────
  function filterList(listId, query) {
    const q = query.toLowerCase();
    document.querySelectorAll(`#${listId} .follow-list-item`).forEach(li => {
      li.style.display = li.textContent.toLowerCase().includes(q) ? '' : 'none';
    });
  }

  document.getElementById('follow-search')?.addEventListener('input', e => {
    filterList('follow-list', e.target.value.trim());
  });
  document.getElementById('communities-modal-search')?.addEventListener('input', e => {
    filterList('communities-modal-list', e.target.value.trim());
  });

  document.getElementById('stat-followers')?.addEventListener('click', () => {
    const s = document.getElementById('follow-search');
    if (s) { s.value = ''; filterList('follow-list', ''); }
  });
  document.getElementById('stat-following')?.addEventListener('click', () => {
    const s = document.getElementById('follow-search');
    if (s) { s.value = ''; filterList('follow-list', ''); }
  });
  document.getElementById('stat-communities')?.addEventListener('click', () => {
    const s = document.getElementById('communities-modal-search');
    if (s) { s.value = ''; filterList('communities-modal-list', ''); }
  });
})();