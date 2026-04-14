const communityList = document.getElementById('create-post-community-list');
const titleInput    = document.getElementById('create-post-title-input');
const titleCount    = document.getElementById('create-post-title-count');
const bodyInput     = document.getElementById('create-post-body-input');
const bodyCount     = document.getElementById('create-post-body-count');

initModal({
  modalId:  'create-post-modal',
  toggleId: 'add-post-toggle',
  closeId:  'create-post-close',
  onOpen() {
    titleInput.value = '';
    bodyInput.value = '';
    titleCount.textContent = '0';
    bodyCount.textContent = '0';
    communityList.classList.remove('open');
  },
  onClose() {
    communityList.classList.remove('open');
  }
});

document.getElementById('create-post-community-btn')
  .addEventListener('click', () => communityList.classList.toggle('open'));

document.querySelector('.create-post-btn-post')
  .addEventListener('click', () => {
    if (titleInput.value.trim() === '') {
      titleInput.classList.add('input-error');
      titleInput.focus();
      return;
    }
    window.location.href = '/post/createPost';
  });

titleInput.addEventListener('input', () => {
  titleCount.textContent = titleInput.value.length;
  titleInput.classList.remove('input-error');
});

bodyInput.addEventListener('input', () => {
  bodyCount.textContent = bodyInput.value.length;
});
