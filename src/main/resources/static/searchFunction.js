initModal({
  modalId:  'search-modal',
  toggleId: 'search-toggle',
  closeId:  'search-close',
  onOpen() {
    const input   = document.getElementById('search-input');
    const results = document.getElementById('search-results');
    input.value       = '';
    results.innerHTML = '';
    input.focus();
  }
});
