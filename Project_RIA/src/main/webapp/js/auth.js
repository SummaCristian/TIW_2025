// ==========================
// Helper Functions
// ==========================

function showForm(formIdToShow) {
  const forms = ['login-form', 'signup1-form', 'signup2-form'];
  forms.forEach(id => {
    const form = document.getElementById(id);
    if (id === formIdToShow) {
      // Reset animation
      form.classList.remove('fade-scale-in');
      void form.offsetWidth; // Force reflow
      form.classList.add('fade-scale-in');
      form.style.display = 'block';
    } else {
      form.style.display = 'none';
    }
  });
}


function displayError(targetId, message) {
  document.getElementById(targetId).textContent = message;
}

// ==========================
// Setup Event Listeners
// ==========================

function setupFormToggles() {
  // Show signup step 1 from login form
  document.getElementById('show-signup1').addEventListener('click', (e) => {
    e.preventDefault();
    showForm('signup1-form');
  });

  // Show signup step 1 from login form
    document.getElementById('show-login').addEventListener('click', (e) => {
      e.preventDefault();
      showForm('login-form');
    });
	
	// Show signup step 1 from login form
	  document.getElementById('show-singup1-back').addEventListener('click', (e) => {
	    e.preventDefault();
	    showForm('signup1-form');
	  });
}

// ==========================
// Form Validation Functions
// ==========================

function setupFormValidation() {
  const signup1Form = document.getElementById('signup1Form');
  signup1Form.addEventListener('submit', (e) => {
    e.preventDefault();
    const username = document.getElementById('signup-username').value.trim();
    const password1 = document.getElementById('signup-password1').value;
    const password2 = document.getElementById('signup-password2').value;

    if (password1 !== password2) {
      displayError('signup1-error', 'Passwords do not match.');
      return;
    }

    // Clear error and move to step 2
    displayError('signup1-error', '');
    showForm('signup2-form');
  });

  const loginForm = document.getElementById('loginForm');
  loginForm.addEventListener('submit', (e) => {
    // Optionally validate login form here
    // Or let it submit normally to the Servlet
  });

  const signup2Form = document.getElementById('signup2Form');
  signup2Form.addEventListener('submit', (e) => {
    // Optional: validate inputs
    // Then let the form submit naturally
  });
}

// ==========================
// Init
// ==========================

document.addEventListener('DOMContentLoaded', () => {
  setupFormToggles();
  setupFormValidation();
  showForm('login-form'); // Default to login form
});