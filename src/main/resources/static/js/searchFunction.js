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

const fmt = s => s ? s.toLowerCase().replace(/_/g, ' ') : '';

document.getElementById('search-input').addEventListener('keydown', async function(e) {
  if (e.key !== 'Enter') return;
  const query = this.value.trim();
  if (!query) return;

  const results = document.getElementById('search-results');
  results.innerHTML = '<li class="search-result-empty">Searching...</li>';

  try {
    const response = await fetch(`/api/search?q=${encodeURIComponent(query)}`, {
      headers: { 'Authorization': 'Bearer ' + localStorage.getItem('jwt') }
    });
    const data = await response.json();
    results.innerHTML = '';

    const hasResults = data.users.length > 0 || data.communities.length > 0 || data.posts.length > 0;

    if (!hasResults) {
      results.innerHTML = `<li class="search-result-empty">No results found for "${query}"</li>`;
      return;
    }

    data.users.forEach(u => {
      const li = document.createElement('li');
      li.className = 'search-result-card';
      li.innerHTML = `
        <div class="src-card-row">
          <img src="/vector-logos/usernameSignIn.svg" alt="" class="src-card-icon">
          <div class="src-card-body">
            <div class="src-card-header">
              <span class="src-card-name">u/${u.username}</span>
            </div>
            ${u.userBio ? `<p class="src-card-desc">${u.userBio}</p>` : ''}
          </div>
        </div>
      `;
      li.addEventListener('click', () => window.location.href = '/profile?user=' + u.username);
      results.appendChild(li);
    });

    data.communities.forEach(c => {
      const li = document.createElement('li');
      li.className = 'search-result-card';
      li.innerHTML = `
        <div class="src-card-row">
          <img src="/vector-logos/clubLogo.svg" alt="" class="src-card-icon">
          <div class="src-card-body">
            <div class="src-card-header">
              <span class="src-card-name">c/${c.communityName}</span>
              ${c.category ? `<span class="mc-card-category">${fmt(c.category)}</span>` : ''}
            </div>
            <span class="src-card-members">${c.memberCount ?? 0} members</span>
            ${c.description ? `<p class="src-card-desc">${c.description}</p>` : ''}
          </div>
        </div>
      `;
      li.addEventListener('click', () => window.location.href = '/community/' + c.communityName);
      results.appendChild(li);
    });

    data.posts.forEach(p => {
      const li = document.createElement('li');
      li.className = 'search-result-card';
      li.innerHTML = `
        <div class="src-card-header">
          <span class="src-card-name">${p.title || p.contentText || ''}</span>
        </div>
        <span class="src-card-members">u/${p.authorUsername}</span>
        ${p.contentText ? `<p class="src-card-desc">${p.contentText}</p>` : ''}
      `;
      results.appendChild(li);
    });

  } catch (err) {
    results.innerHTML = '<li class="search-result-empty">Could not connect to server.</li>';
  }
});
