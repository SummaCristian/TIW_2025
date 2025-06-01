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
	document.getElementById('show-signup1').addEventListener('click', (e) => {
		e.preventDefault();
		showForm('signup1-form');
	});

	document.getElementById('show-login').addEventListener('click', (e) => {
		e.preventDefault();
		showForm('login-form');
	});

	document.getElementById('show-singup1-back').addEventListener('click', (e) => {
		e.preventDefault();
		showForm('signup1-form');
	});
}

// ==========================
// Form Validation Functions
// ==========================

function setupLoginForm() {
	var loginForm = document.getElementById('loginForm');
	loginForm.addEventListener('submit', function(e) {
		e.preventDefault();

		var username = document.getElementById('login-username').value.trim();
		var password = document.getElementById('login-password').value;

		var xhr = new XMLHttpRequest();
		xhr.open('POST', 'LoginServlet', true);
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

		xhr.onreadystatechange = function() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				if (xhr.status === 200) {
					// Forward to HomePageServlet
					window.location.href = 'home';
				} else if (xhr.status === 401) {
					displayError('login-error', xhr.responseText || 'Invalid credentials');
				} else {
					displayError('login-error', 'An unexpected error occurred.');
				}
			}
		};

		var params = 'username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password);
		xhr.send(params);
	});
}

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

		displayError('signup1-error', '');
		showForm('signup2-form');
	});

	const signup2Form = document.getElementById('signup2Form');
	signup2Form.addEventListener('submit', (e) => {
		// Optional: validate inputs before letting it submit naturally
	});
}

// ==========================
// Init
// ==========================

setupFormToggles();
setupFormValidation();
setupLoginForm();
showForm('login-form'); // Default to login form