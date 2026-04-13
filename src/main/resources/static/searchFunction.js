const modal = document.getElementById('search-modal');
const toggle = document.getElementById('search-toggle');
const closeBtn = document.getElementById('search-close');
const input = document.getElementById('search-input');
const results = document.getElementById('search-results');

function openModal() {
    modal.classList.add('active');
    modal.setAttribute('aria-hidden', 'false');
    input.value = '';
    results.innerHTML = '';
    input.focus();
}

function closeModal() {
    modal.classList.remove('active');
    modal.setAttribute('aria-hidden', 'true');
}

toggle.addEventListener('click', e => { e.preventDefault(); openModal(); });
closeBtn.addEventListener('click', closeModal);
modal.addEventListener('click', e => {
    if (e.target === modal) closeModal();
});
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeModal();
});
