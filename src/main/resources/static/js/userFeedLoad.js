// Loads the main feed page (/feed).
// Delegates post card rendering and modal to postCardRenderer.js.

(function () {
  const token   = localStorage.getItem('token');
  const headers = token ? { 'Authorization': 'Bearer ' + token } : {};

  if (!token) return;

  initPostViewModal();

  const urlPost = new URLSearchParams(window.location.search).get('post');
  if (urlPost) {
    history.replaceState(null, '', '/feed');
    fetch(`/api/posts/${urlPost}`, { headers })
      .then(r => r.ok ? r.json() : null)
      .then(post => { if (post) openPostViewModal(post); })
      .catch(() => {});
  }

  fetch('/api/profile/me', { headers })
    .then(r => r.ok ? r.json() : Promise.reject())
    .then(profile => {
      const currentUserId = profile.userId;
      if (!currentUserId) return;
      localStorage.setItem('currentUserId', currentUserId);
		
		// ── feed posts ────────────────────────────────────────────────
		      Promise.all([
		        fetch(`/api/posts/feed/${currentUserId}`, { headers }).then(r => r.ok ? r.json() : []),
		        fetch(`/api/posts/feed/${currentUserId}/type`, { headers }).then(r => r.ok ? r.json() : { type: 'feed' })
		      ])
			  .then(([posts, { type }]) => {
			    const header = document.getElementById('feed-header-title');
			    if (header) {
			      header.textContent = 'For You';
			    }
			    if (type === 'suggested') {
			      posts.forEach(p => p.suggested = true);
			    }
			    renderPostList(posts, 'feed-posts-list');
			  })
		      .catch(() => renderPostList([], 'feed-posts-list'));

		    })
		    .catch(() => renderPostList([], 'feed-posts-list'));

		})();