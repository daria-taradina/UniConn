(function () {
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

  fetch('/api/community/trending-tags')
    .then(r => r.ok ? r.json() : [])
    .then(renderTrending)
    .catch(() => {});
})();
