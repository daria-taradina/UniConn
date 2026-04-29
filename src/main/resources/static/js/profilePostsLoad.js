// Loads post cards on the logged-in user's own profile page (/profile).
// Delegates post card rendering to postCardRenderer.js.
(function () {
  const token  = localStorage.getItem('token');
  const userId = localStorage.getItem('currentUserId');

  if (!token || !userId) {
    const container = document.getElementById('profile-posts-container');
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

  fetch(`/api/posts/profile/${userId}`, {
    headers: { 'Authorization': 'Bearer ' + token }
  })
    .then(res => { if (!res.ok) throw new Error(); return res.json(); })
    .then(posts => {
      renderPostList(posts, 'profile-posts-container');
      openPendingPostModal();
    })
    .catch(() => {
      const container = document.getElementById('profile-posts-container');
      if (container) container.innerHTML = '<p class="profile-posts-empty">Could not load posts.</p>';
    });
})();
