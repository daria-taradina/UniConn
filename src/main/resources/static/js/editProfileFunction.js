const editNameInput    = document.getElementById('edit-name-input');
const editBioInput     = document.getElementById('edit-bio-input');
const editMsg          = document.getElementById('edit-profile-message');
const editPictureInput = document.getElementById('edit-picture-input');
const editAvatarPreview = document.getElementById('edit-avatar-preview');

// sync preview with current avatar on modal open
initModal({
  modalId:  'edit-profile-modal',
  toggleId: 'edit-profile-toggle',
  closeId:  'edit-profile-close',
  onOpen() {
    editMsg.style.display = 'none';
    editMsg.className = 'profile-message';
    editPictureInput.value = '';
    const currentAvatar = document.getElementById('profile-picture-img');
    if (editAvatarPreview && currentAvatar) editAvatarPreview.src = currentAvatar.src;
  },
  onClose() {}
});

// show preview immediately when user picks a file
editPictureInput.addEventListener('change', () => {
  const file = editPictureInput.files[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = e => { editAvatarPreview.src = e.target.result; };
  reader.readAsDataURL(file);
});

document.getElementById('edit-profile-submit').addEventListener('click', async () => {
  editMsg.style.display = 'none';
  editMsg.className = 'profile-message';

  const file = editPictureInput.files[0];

  // upload picture first if one was chosen
  if (file) {
    const allowed = ['image/jpeg', 'image/png', 'image/webp'];
    if (!allowed.includes(file.type)) {
      editMsg.textContent = 'Only JPG, PNG, or WebP images are allowed.';
      editMsg.classList.add('error');
      editMsg.style.display = 'block';
      return;
    }
    if (file.size > 2 * 1024 * 1024) {
      editMsg.textContent = 'Image must be 2MB or smaller.';
      editMsg.classList.add('error');
      editMsg.style.display = 'block';
      return;
    }
    const formData = new FormData();
    formData.append('file', file);

    try {
      // Step 1: upload to Cloudinary
      const uploadRes = await fetch('/api/upload/user', {
        method: 'POST',
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
        body: formData
      });

      if (!uploadRes.ok) {
        editMsg.textContent = 'Failed to upload picture. Please try again.';
        editMsg.classList.add('error');
        editMsg.style.display = 'block';
        return;
      }

      const cloudinaryUrl = await uploadRes.text();

      // Step 2: save the Cloudinary URL to the user account
      const saveRes = await fetch('/api/users/me/picture', {
        method: 'PATCH',
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('token'),
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cloudinaryUrl)
      });

      if (saveRes.ok) {
        const avatarEl = document.getElementById('profile-picture-img');
        if (avatarEl && editAvatarPreview) avatarEl.src = editAvatarPreview.src;
      } else {
        editMsg.textContent = 'Failed to save picture. Please try again.';
        editMsg.classList.add('error');
        editMsg.style.display = 'block';
        return;
      }
    } catch {
      editMsg.textContent = 'Something went wrong uploading the picture.';
      editMsg.classList.add('error');
      editMsg.style.display = 'block';
      return;
    }
  }

  // update name and bio
  const body = {
    name:    editNameInput.value.trim(),
    userBio: editBioInput.value.trim()
  };

  try {
    const res = await fetch('/api/profile/update', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + localStorage.getItem('token') },
      body: JSON.stringify(body)
    });

    if (res.ok) {
      editMsg.textContent = 'Profile updated successfully.';
      editMsg.classList.add('success');
      editMsg.style.display = 'block';

      const bioSection = document.querySelector('.profile-bio-section');
      bioSection.innerHTML = '';
      if (body.name) {
        const nameEl = document.createElement('span');
        nameEl.className = 'profile-name';
        nameEl.textContent = body.name;
        bioSection.appendChild(nameEl);
      }
      if (body.userBio) {
        const bioEl = document.createElement('p');
        bioEl.className = 'profile-bio';
        bioEl.textContent = body.userBio;
        bioSection.appendChild(bioEl);
      }
      if (!body.name && !body.userBio) {
        const emptyEl = document.createElement('p');
        emptyEl.className = 'profile-bio-empty';
        emptyEl.textContent = 'No bio yet.';
        bioSection.appendChild(emptyEl);
      }
    } else {
      editMsg.textContent = 'Failed to update profile. Please try again.';
      editMsg.classList.add('error');
      editMsg.style.display = 'block';
    }
  } catch {
    editMsg.textContent = 'Something went wrong. Please try again.';
    editMsg.classList.add('error');
    editMsg.style.display = 'block';
  }
});
