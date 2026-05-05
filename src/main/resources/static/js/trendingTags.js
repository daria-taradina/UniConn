// Shared trending tags loader — call on any page that has #trending-tags-list
function loadTrendingTags() {
  const list = document.getElementById('trending-tags-list');
  if (!list) return;

  const token = localStorage.getItem('token');
  const headers = token ? { 'Authorization': 'Bearer ' + token } : {};

  fetch('/api/posts/trending', { headers })
    .then(r => r.ok ? r.json() : [])
    .then(tags => {
      if (!tags || tags.length === 0) {
        list.innerHTML = '<li class="trending-tag-empty">No trending topics yet.</li>';
        return;
      }
      list.innerHTML = '';
      tags.slice(0, 5).forEach((tag, i) => {
        const li = document.createElement('li');
        li.className = 'trending-tag-item';
        li.innerHTML = `<span class="trending-tag-rank">#${i + 1}</span><span class="trending-tag-name">${tag.tagName}</span>`;
        li.addEventListener('click', () => {
          if (typeof openTagPostsModal === 'function') openTagPostsModal(tag.tagName);
        });
        list.appendChild(li);
      });
    })
    .catch(() => {
      list.innerHTML = '<li class="trending-tag-empty">No trending topics yet.</li>';
    });
}

document.addEventListener('DOMContentLoaded', loadTrendingTags);