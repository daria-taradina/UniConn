// Loads the main feed page (/feed).
// Delegates post card rendering to postCardRenderer.js.
(function () {
  const token = localStorage.getItem('token');
  const headers = token ? { 'Authorization': 'Bearer ' + token } : {};

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

  if (!token) return;

  initPostViewModal();

  fetch('/api/profile/me', { headers })
    .then(r => r.ok ? r.json() : null)
    .then(profile => {
      if (!profile) return;

      return Promise.all([
        fetch(`/api/posts/feed/${profile.userId}`, { headers }).then(r => r.ok ? r.json() : []),
        fetch('/api/community/trending-tags').then(r => r.ok ? r.json() : [])
      ]).then(([posts, trendingTags]) => {
        renderPostList(posts, 'feed-posts-container');
        renderTrending(trendingTags);
      });
    })
    .catch(() => {
      const placeholder = document.getElementById('feed-posts-placeholder');
      if (placeholder) placeholder.textContent = 'Could not load posts.';
    });
})();
