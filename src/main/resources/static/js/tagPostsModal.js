// Injects and manages the tag posts modal. Loaded on every page.
// window.openTagPostsModal(tag) is called by trending tags, post tags, and community tags.
(function () {
  const MODAL_HTML = `
    <div id="tag-posts-modal" class="search-modal-overlay" aria-hidden="true">
      <div class="search-modal">
        <div class="header-right">
          <h3 class="search-modal-title" id="tag-posts-modal-title">#tag</h3>
        </div>
        <button class="search-modal-close" id="tag-posts-modal-close" aria-label="Close">&times;</button>
        <div class="tag-modal-toolbar">
          <div class="tag-modal-filters">
            <button class="tag-filter-btn active" data-filter="all">All</button>
            <button class="tag-filter-btn" data-filter="communities">Communities</button>
            <button class="tag-filter-btn" data-filter="posts">Posts</button>
          </div>
        </div>
        <div id="tag-posts-modal-list" style="display:flex;flex-direction:column;gap:10px;margin-top:8px;overflow-y:auto;max-height:560px;"></div>
      </div>
    </div>
  `;

  let allPosts       = [];
  let allCommunities = null;
  let activeFilter   = 'all';
  let searchQuery    = '';
  let currentTag     = '';

  // Fetch all communities once and cache them
  async function loadCommunities() {
    if (allCommunities !== null) return allCommunities;
    const token   = localStorage.getItem('token');
    const headers = token ? { 'Authorization': 'Bearer ' + token } : {};
    try {
      const r = await fetch('/api/community/all', { headers });
      allCommunities = r.ok ? await r.json() : [];
    } catch {
      allCommunities = [];
    }
    return allCommunities;
  }

  function buildCommunityCard(c) {
    const div = document.createElement('div');
    div.className = 'search-result-card';
    div.style.cursor = 'pointer';
    div.addEventListener('click', () => { window.location.href = '/community/' + c.communityName; });
    const tagsHtml = Array.isArray(c.tags) && c.tags.length
      ? '<div style="display:flex;flex-wrap:wrap;gap:4px;margin-top:4px">'
        + c.tags.map(t => `<span class="post-card-tag" style="cursor:pointer" onclick="event.stopPropagation();openTagPostsModal('${t}')">#${t}</span>`).join('')
        + '</div>'
      : '';
    div.innerHTML = `
      <div class="src-card-row">
        <img src="${c.communityPicture || '/vector-logos/clubLogo.svg'}" alt="" class="src-card-icon">
        <div class="src-card-body">
          <div class="src-card-header">
            <span class="src-card-name">c/${c.communityName || ''}</span>
          </div>
          <span class="src-card-members">${c.memberCount ?? 0} members</span>
          ${c.description ? `<p class="src-card-desc">${c.description}</p>` : ''}
          ${tagsHtml}
        </div>
      </div>
    `;
    return div;
  }

  function sectionHeader(label) {
    const div = document.createElement('div');
    div.style.cssText = 'padding:8px 4px 2px;font-size:0.75em;font-weight:700;color:#888;letter-spacing:0.06em;text-transform:uppercase';
    div.textContent = label;
    return div;
  }

  function filterCommunities(communities) {
    const q = searchQuery.toLowerCase();
    return communities.filter(c =>
      Array.isArray(c.tags) && c.tags.some(t => t.toLowerCase() === currentTag.toLowerCase()) &&
      (!q || (c.communityName || '').toLowerCase().includes(q) || (c.description || '').toLowerCase().includes(q))
    );
  }

  function filterPosts(posts) {
    const q = searchQuery.toLowerCase();
    return q
      ? posts.filter(p =>
          (p.authorUsername || '').toLowerCase().includes(q) ||
          (p.title || '').toLowerCase().includes(q) ||
          (p.contentText || '').toLowerCase().includes(q))
      : posts;
  }

  function renderCommunityCards(communities) {
    const list = document.getElementById('tag-posts-modal-list');
    if (!list) return;
    list.innerHTML = '';
    const filtered = filterCommunities(communities);
    if (!filtered.length) {
      list.innerHTML = '<p style="padding:12px 8px;color:#999;font-size:0.9em">No communities found.</p>';
      return;
    }
    filtered.forEach(c => list.appendChild(buildCommunityCard(c)));
  }

  function renderPostCards(posts) {
    const list = document.getElementById('tag-posts-modal-list');
    if (!list) return;
    list.innerHTML = '';
    const filtered = filterPosts(posts);
    if (!filtered.length) {
      list.innerHTML = '<p style="padding:12px 8px;color:#999;font-size:0.9em">No posts found.</p>';
      return;
    }
    filtered.forEach(post => list.appendChild(createPostCard(post)));
  }

  function applyFilters() {
    const list = document.getElementById('tag-posts-modal-list');
    if (!list) return;

    if (activeFilter === 'communities') {
      list.innerHTML = '<p style="padding:12px 8px;color:#999;font-size:0.9em">Loading...</p>';
      loadCommunities().then(renderCommunityCards);
    } else if (activeFilter === 'posts') {
      renderPostCards(allPosts);
    } else {
      // 'all' — communities section + posts section
      list.innerHTML = '<p style="padding:12px 8px;color:#999;font-size:0.9em">Loading...</p>';
      loadCommunities().then(communities => {
        list.innerHTML = '';
        const comms = filterCommunities(communities);
        const posts  = filterPosts(allPosts);
        if (!comms.length && !posts.length) {
          list.innerHTML = '<p style="padding:12px 8px;color:#999;font-size:0.9em">No results found.</p>';
          return;
        }
        if (comms.length) {
          list.appendChild(sectionHeader('Communities'));
          comms.forEach(c => list.appendChild(buildCommunityCard(c)));
        }
        if (posts.length) {
          list.appendChild(sectionHeader('Posts'));
          posts.forEach(post => list.appendChild(createPostCard(post)));
        }
      });
    }
  }

  document.addEventListener('DOMContentLoaded', () => {
    document.body.insertAdjacentHTML('beforeend', MODAL_HTML);

    const modal    = document.getElementById('tag-posts-modal');
    const closeBtn = document.getElementById('tag-posts-modal-close');

    function close() {
      modal.classList.remove('active');
      modal.setAttribute('aria-hidden', 'true');
    }

    closeBtn.addEventListener('click', close);
    modal.addEventListener('click', e => { if (e.target === modal) close(); });
    document.addEventListener('keydown', e => { if (e.key === 'Escape') close(); });

    document.querySelectorAll('.tag-filter-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        document.querySelectorAll('.tag-filter-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        activeFilter = btn.dataset.filter;
        applyFilters();
      });
    });

    // Global hashtag delegation — capture phase so it fires before card click handlers
    document.addEventListener('click', e => {
      const tagEl = e.target.closest('.mc-tag, .community-tag, .post-card-tag');
      if (!tagEl) return;
      e.stopPropagation();
      e.preventDefault();
      const tag = tagEl.textContent.replace(/^#/, '').trim();
      if (tag) openTagPostsModal(tag);
    }, true);
  });

  window.openTagPostsModal = function (tag) {
    const modal = document.getElementById('tag-posts-modal');
    const title = document.getElementById('tag-posts-modal-title');
    const list  = document.getElementById('tag-posts-modal-list');
    if (!modal) return;

    currentTag   = tag;
    activeFilter = 'all';
    searchQuery  = '';
    allPosts     = [];

    title.textContent = '#' + tag;
    list.innerHTML = '<p style="padding:12px 8px;color:#999;font-size:0.9em">Loading...</p>';
    document.querySelectorAll('.tag-filter-btn').forEach(b => {
      b.classList.toggle('active', b.dataset.filter === 'all');
    });

    modal.classList.add('active');
    modal.setAttribute('aria-hidden', 'false');

    const token   = localStorage.getItem('token');
    const headers = token ? { 'Authorization': 'Bearer ' + token } : {};

    fetch('/api/posts/tag/' + encodeURIComponent(tag), { headers })
      .then(r => r.ok ? r.json() : [])
      .then(posts => { allPosts = posts; applyFilters(); })
      .catch(() => {
        list.innerHTML = '<p style="padding:12px 8px;color:#999;font-size:0.9em">Failed to load posts.</p>';
      });
  };
})();