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

titleInput.addEventListener('input', () => {
  titleCount.textContent = titleInput.value.length;
});

bodyInput.addEventListener('input', () => {
  bodyCount.textContent = bodyInput.value.length;
});
