// intercepts login form submission and sends credentials to backend API.
// success: stores returned JWT in localStorage and redirects to feed
// failure: shows error in HTML
document.getElementById('login-form').addEventListener('submit', async (e) => {
  e.preventDefault();

  const email    = document.querySelector('input[name="email"]').value.trim();
  const password = document.querySelector('input[name="password"]').value;
  const errorEl  = document.getElementById('login-error');

  errorEl.style.display = 'none'; // hides any previous error

  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      // backend expects csunEmail, not email
      body: JSON.stringify({ csunEmail: email, password })
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('jwt', data.token); // save JWT for future API calls
      window.location.href = '/feed';
    } else {
      errorEl.style.display = 'block'; 
    }
  } catch {
    errorEl.style.display = 'block';
  }
});
