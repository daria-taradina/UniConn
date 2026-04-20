const communityNameInput     = document.getElementById('community-name-input');
const communityDescInput     = document.getElementById('community-desc-input');
const communityCategoryInput = document.getElementById('community-category-input');
const communityTagsInput     = document.getElementById('community-tags-input');

// init modal: reset fields when opened
initModal({
  modalId:  'create-community-modal',
  toggleId: 'create-community-toggle',
  closeId:  'create-community-close',
  onOpen() {
    communityNameInput.value     = '';
    communityDescInput.value     = '';
    communityCategoryInput.value = ''; // resets dropdown to placeholder
    communityTagsInput.value     = '';
    communityNameInput.classList.remove('input-error');
    communityDescInput.classList.remove('input-error');
    communityCategoryInput.classList.remove('input-error');
  }
});

document.getElementById('create-community-submit').addEventListener('click', async () => {
  // validate required fields before sending to backend
  if (communityNameInput.value.trim() === '') {
    communityNameInput.classList.add('input-error');
    communityNameInput.focus();
    return;
  }
  if (communityDescInput.value.trim() === '') {
    communityDescInput.classList.add('input-error');
    communityDescInput.focus();
    return;
  }
  if (communityCategoryInput.value === '') {
    communityCategoryInput.classList.add('input-error');
    return;
  }

  // parse tags: split by comma, trim whitespace, drop empty strings if any
  // tags are optional so an empty input just sends an empty array
  const tags = communityTagsInput.value.trim()
    ? communityTagsInput.value.split(',').map(t => t.trim()).filter(t => t !== '')
    : [];

  try {
    const response = await fetch('/api/community/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('jwt') // JWT required
      },
      // send communityDTYO fields: name, description, category enum value, tags array
      body: JSON.stringify({
        communityName: communityNameInput.value.trim(),
        description:   communityDescInput.value.trim(),
        category:      communityCategoryInput.value, // matches CommunityCategory enum (e.g. "ACADEMICS")
        tags
      })
    });

    if (response.ok) {
      window.location.reload(); // refresh page to show new community
    } else {
      const msg = await response.text();
      alert(msg || 'Failed to create community.');
    }
  } catch {
    alert('Something went wrong. Please try again.');
  }
});

// clear error highlight as soon as the user corrects a field
communityNameInput.addEventListener('input', () => communityNameInput.classList.remove('input-error'));
communityDescInput.addEventListener('input', () => communityDescInput.classList.remove('input-error'));
communityCategoryInput.addEventListener('change', () => communityCategoryInput.classList.remove('input-error'));
