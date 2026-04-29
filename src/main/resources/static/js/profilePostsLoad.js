// Loads post cards on the logged-in user's own profile page (/profile).
// Delegates post card rendering to postCardRenderer.js.

(function () {
  const token  = localStorage.getItem('token');
  const userId = localStorage.getItem('currentUserId');
  const headers = { 'Authorization': 'Bearer ' + token };
  const container = document.getElementById('profile-posts-container');

  if (!token || !userId) {
    if (container) container.innerHTML = '<p class="profile-posts-empty">No posts yet.</p>';
    return;
  }

  initPostViewModal();

  function openPendingPostModal() {
    const raw = sessionStorage.getItem('pendingPostModal');
    if (!raw) return;
    sessionStorage.removeItem('pendingPostModal');
    try {
      const post = JSON.parse(raw);
      if (typeof openPostViewModal === 'function') openPostViewModal(post);
    } catch {}
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
      renderPostList(posts, 'profile-posts-container', bar);
    });

    bar.appendChild(select);
    return bar;
  }

  // ── render with filter bar preserved ─────────────────────────────
  function renderPostList(posts, containerId, filterBar) {
    const c = document.getElementById(containerId);
    if (!c) return;
    c.innerHTML = '';
    if (filterBar) c.appendChild(filterBar);
    if (!posts || posts.length === 0) {
      const empty = document.createElement('p');
      empty.className = 'profile-posts-empty';
      empty.style.color = '#999';
      empty.style.fontSize = '0.9em';
      empty.style.padding = '16px';
      empty.textContent = 'No posts yet.';
      c.appendChild(empty);
      return;
    }
    posts.forEach(post => c.appendChild(createPostCard(post)));
  }

  // ── fetch both post types in parallel ────────────────────────────
  Promise.all([
    fetch(`/api/posts/profile/${userId}`, { headers }).then(r => r.ok ? r.json() : []),
    fetch(`/api/posts/user/${userId}/community`, { headers }).then(r => r.ok ? r.json() : [])
  ])
    .then(([profilePosts, communityPosts]) => {
      const allPosts = [...profilePosts, ...communityPosts]
        .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

      const filterBar = createFilterBar(allPosts, profilePosts, communityPosts);
      renderPostList(allPosts, 'profile-posts-container', filterBar);
      openPendingPostModal();
    })
    .catch(() => {
      if (container) container.innerHTML = '<p style="color:#999;font-size:0.9em;padding:16px">Could not load posts.</p>';
    });
})();