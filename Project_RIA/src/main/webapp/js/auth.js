// ==========================
// Helper Functions
// ==========================

// Changes the current view to the form with ID formIdToShow
function showForm(formIdToShow) {
	// Clean all the Error Texts in the forms
	cleanErrorTexts();
	
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

// Sets the error text with ID targetID to display the text passed in message
function displayError(targetId, message) {
	document.getElementById(targetId).textContent = message;
}

// Resets all the error texts in all the Forms
function cleanErrorTexts() {
	const errorTexts = ['login-error', 'signup1-error', 'signup2-error'];
	errorTexts.forEach(id => {
		displayError(id, '');
	});
}

// ==========================
// Setup Event Listeners
// ==========================

// Setups the Buttons to change which Form is currently shown, thus enabling navigation
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

// Setup the Login Form and the function that handles the Submit/Login process
function setupLoginForm() {
	// Retrieve the Form
	var loginForm = document.getElementById('loginForm');
	// Add an event listener that will be triggered when the User submits the form
	loginForm.addEventListener('submit', function(e) {
		// Prevents HTML's default behavior
		e.preventDefault();

		// Data inside the form's inputs
		var username = document.getElementById('login-username').value.trim();
		var password = document.getElementById('login-password').value;

		// Create the request
		var xhr = new XMLHttpRequest();
		xhr.open('POST', 'LoginServlet', true);
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

		// Attach the callback function to handle the result of this request
		xhr.onreadystatechange = function() {
			if (xhr.readyState === XMLHttpRequest.DONE) {
				// Request completed, handle the results
				if (xhr.status === 200) {
					// SUCCESS
					// Forward to HomePageServlet
					window.location.href = 'home';
				} else if (xhr.status === 400) {
					// ERROR 400: BAD REQUEST
					displayError('login-error', xhr.responseText || 'Missing parameters');
				} else if (xhr.status === 401) {
					// ERROR 401: UNAUTORIZED
					displayError('login-error', xhr.responseText || 'Invalid credentials');
				} else if (xhr.status === 500) {
					// ERROR 500: INTERNAL SERVER ERROR
					displayError('login-error', xhr.responseText || 'Internal Server Error');
				} else {
					// OTHER: DEFAULT ERROR
					displayError('login-error', 'An unexpected error occurred.');
				}
			}
		};

		// Add the inputs to the request as parameters
		var params = 'username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password);
		// Send the request to the Server
		xhr.send(params);
	});
}

// Setups the SignUp forms' logic for both phase 1 and 2
function setupFormValidation() {
	// Retrieves Form 1
    const signup1Form = document.getElementById('signup1Form');
	// Attaches a callback function to handle the submit
    signup1Form.addEventListener('submit', function(e) {
		// Prevents the HTML default behavior
        e.preventDefault();
		// Retrieves the data inside the form's inputs
        const username = document.getElementById('signup-username').value.trim();
        const password1 = document.getElementById('signup-password1').value;
        const password2 = document.getElementById('signup-password2').value;

		// Checks if the 2 passwords are the same
        if (password1 !== password2) {
			// They are not the same, show an error message
            displayError('signup1-error', 'Passwords do not match.');
            return;
        }

		// Ok, remove previous errors if there is any
        cleanErrorTexts();

        // Send Phase 1 data to backend
		// Create the Request
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'SignUpServlet', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

		// Attach the callback function to handle the results
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
					// SUCCESS: store data for phase 2 inside Form 2's hidden inputs
					document.getElementById('signup2-username').value = username;
					document.getElementById('signup2-password1').value = password1;
					document.getElementById('signup2-password2').value = password2;

					// Trigger automatic navigation to the second Form
                    showForm('signup2-form');
                } else {
					// Error received from the server, display it in the error text
                    displayError('signup1-error', xhr.responseText || 'An error occurred during signup.');
                }
            }
        };

		// Add data to the request as parameters
        var params = 'phase=1' +
                     '&username=' + encodeURIComponent(username) +
                     '&password1=' + encodeURIComponent(password1) +
                     '&password2=' + encodeURIComponent(password2);
		
		// Send the Request
        xhr.send(params);
    });

	// Retrieves Form 2
    const signup2Form = document.getElementById('signup2Form');
	// Attaches a callback function to handle submit
    signup2Form.addEventListener('submit', function(e) {
		// Prevents HTML's default behavior
        e.preventDefault();

		// Retrieves the data from the Form's inputs
        const firstName = document.getElementById('signup-firstName').value.trim();
        const lastName = document.getElementById('signup-lastName').value.trim();
        const address = document.getElementById('signup-address').value.trim();

		// Checks if they are not missing
        if (!firstName || !lastName || !address) {
			// Something is missing, display the error
            displayError('signup2-error', 'Please fill all fields.');
            return;
        }

		// Retrieves the rest of the data from phase 1
		const username = document.getElementById('signup2-username').value;
		const password1 = document.getElementById('signup2-password1').value;
		const password2 = document.getElementById('signup2-password2').value;

		// Create the Request
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'SignUpServlet', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

		// Attach a callback function to handle the response
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // SUCCESS: redirect to login or home
                    window.location.href = 'login';
                } else {
					// ERROR: display error message from Server
					const errorText = xhr.responseText || 'An error occurred during signup.';

					if (errorText.includes('Invalid credentials')) {
						// Something happened to the credentials validated in phase 1
					    // Phase 1 error: go back to Form 1 and show error
					    showForm('signup1-form');
					    displayError('signup1-error', errorText);
					} else {
					    // Phase 2 error: show error in Phase 2 form
					    displayError('signup2-error', errorText);
					}
                }
            }
        };

		// Put data into the request as parameters
        var params = 'phase=2' +
                     '&username=' + encodeURIComponent(username) +
                     '&password1=' + encodeURIComponent(password1) +
                     '&password2=' + encodeURIComponent(password2) +
                     '&firstName=' + encodeURIComponent(firstName) +
                     '&lastName=' + encodeURIComponent(lastName) +
                     '&address=' + encodeURIComponent(address);
		
		// Send the Request
        xhr.send(params);
    });
}

// ==========================
// Init
// ==========================

setupFormToggles();
setupFormValidation();
setupLoginForm();
showForm('login-form'); // Default to login form