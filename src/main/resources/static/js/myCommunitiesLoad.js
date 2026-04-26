(function () {
  const list = document.getElementById('my-communities-list');
  if (!list) return;

  let activeCategory = 'all';

  function applyFilters() {
    const q = (document.getElementById('communities-search')?.value || '').trim().toLowerCase();
    list.querySelectorAll('.mc-card').forEach(card => {
      const name = (card.querySelector('.mc-card-name')?.textContent || '').toLowerCase();
      const tags = (card.querySelector('.mc-card-tags')?.textContent || '').toLowerCase();
      const cat  = (card.dataset.category || '').toLowerCase();
      const matchesSearch   = !q || name.includes(q) || tags.includes(q);
      const matchesCategory = activeCategory === 'all' || cat === activeCategory;
      card.style.display = (matchesSearch && matchesCategory) ? '' : 'none';
    });
  }

  const searchInput = document.getElementById('communities-search');
  if (searchInput) searchInput.addEventListener('input', applyFilters);

  document.querySelectorAll('.mc-filter-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.mc-filter-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      activeCategory = btn.dataset.category;
      applyFilters();
    });
  });

  document.getElementById('my-communities-btn')?.classList.add('active');

  const token = localStorage.getItem('token');
  const headers = token ? { 'Authorization': 'Bearer ' + token } : {};
  const currentUsername = localStorage.getItem('currentUsername') || '';

  const fmt = s => s ? s.toLowerCase().replace(/_/g, ' ') : '';

  function render(communities) {
    if (communities.length === 0) {
      list.innerHTML = '<p class="my-communities-empty">You have not joined or created any communities yet.</p>';
      return;
    }

    communities.forEach(c => {
      const isAdmin = c.createdByUsername === currentUsername;
      const card = document.createElement('div');
      card.className = 'mc-card';
      card.dataset.category = (c.category || '').toLowerCase();

      const tags = Array.isArray(c.tags) && c.tags.length > 0
        ? c.tags.map(t => `<span class="mc-tag">#${t}</span>`).join('')
        : '';

      card.innerHTML = `
        <div class="mc-card-left">
          <img src="/vector-logos/clubLogo.svg" alt="" class="mc-card-icon">
        </div>
        <div class="mc-card-body">
          <div class="mc-card-header">
            <span class="mc-card-name">c/${c.communityName || ''}</span>
            <span class="mc-card-category">${fmt(c.category)}</span>
            ${!isAdmin ? `<button class="join-leave-btn leave-btn" data-id="${c.communityId}">Leave</button>` : ''}
          </div>
          <span class="mc-card-members">${c.memberCount ?? 1} members</span>
          <p class="mc-card-desc">${c.description || ''}</p>
          ${tags ? `<div class="mc-card-tags">${tags}</div>` : ''}
        </div>
      `;

      card.addEventListener('click', (e) => {
        if (e.target.closest('.join-leave-btn')) return;
        sessionStorage.setItem('communityDetail', JSON.stringify(c));
        window.location.href = '/community/' + c.communityName;
      });

      if (!isAdmin) {
        const btn = card.querySelector('.join-leave-btn');
        btn.addEventListener('click', async (e) => {
          e.stopPropagation();
          const leaving = btn.classList.contains('leave-btn');
          const res = await fetch(`/api/community/${c.communityId}/${leaving ? 'leave' : 'join'}`, {
            method: leaving ? 'DELETE' : 'POST',
            headers
          });
          if (res.ok) {
            btn.textContent = leaving ? 'Join' : 'Leave';
            btn.className = 'join-leave-btn ' + (leaving ? 'join-btn' : 'leave-btn');
          } else {
            const data = await res.json().catch(() => ({}));
            alert(data.error || 'Action failed.');
          }
        });
      }

      list.appendChild(card);
    });
  }

  Promise.all([
    fetch('/api/community/my-communities', { headers })
      .then(res => { if (!res.ok) throw new Error(); return res.json(); }),
    fetch('/api/community/trending-tags')
      .then(res => res.ok ? res.json() : [])
  ]).then(([communities, trendingTags]) => {
    render(communities);
    renderTrending(trendingTags);
  }).catch(() => {
    const local = JSON.parse(localStorage.getItem('myCommunities') || '[]');
    render(local);
  });

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
      li.addEventListener('click', () => { window.location.href = '/communities?tag=' + encodeURIComponent(tag); });
      trendingList.appendChild(li);
    });
  }
})();
