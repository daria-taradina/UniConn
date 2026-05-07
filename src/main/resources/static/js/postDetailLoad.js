(function () {
  const postId = window.location.pathname.split('/').filter(Boolean).pop();
  if (postId) sessionStorage.setItem('pendingPostModal', postId);
  window.location.replace('/feed');
})();
