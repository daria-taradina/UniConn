const editNameInput = document.getElementById('edit-name-input');
const editBioInput  = document.getElementById('edit-bio-input');
const editMsg       = document.getElementById('edit-profile-message');

initModal({
  modalId:  'edit-profile-modal',
  toggleId: 'edit-profile-toggle',
  closeId:  'edit-profile-close',
  onOpen() {
    editMsg.style.display = 'none';
    editMsg.className = 'profile-message';
  },
  onClose() {}
});

document.getElementById('edit-profile-submit').addEventListener('click', async () => {
  // reset message before each save attempt
  editMsg.style.display = 'none';
  editMsg.className = 'profile-message';

  // send only the editName and editBio
  const body = {
    name:    editNameInput.value.trim(),
    userBio: editBioInput.value.trim()
  };

  try {
    const res = await fetch('/api/profile/update', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('jwt') // JWT required
      },
      body: JSON.stringify(body)
    });

    if (res.ok) {
      editMsg.textContent = 'Profile updated successfully.';
      editMsg.classList.add('success');
      editMsg.style.display = 'block';
      setTimeout(() => window.location.reload(), 1000); // reload to reflect changes
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
