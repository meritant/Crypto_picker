document.getElementById('loginForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            hideError();
            
            const data = {
                email: document.getElementById('email').value,
                password: document.getElementById('password').value
            };

            try {
                const response = await fetch('/api/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const result = await response.json();
                    localStorage.setItem('token', result.token);
                    localStorage.setItem('userEmail', result.email);
                    window.location.href = '/';  // Redirect to index instead of dashboard
                } else {
                    const error = await response.json();
                    showError(error.message || 'Login failed. Please check your credentials.');
                }
            } catch (error) {
                console.error('Error:', error);
                showError('Error during login. Please try again.');
            }
        });

        function showError(message) {
            const errorAlert = document.getElementById('errorAlert');
            const errorMessage = document.getElementById('errorMessage');
            errorMessage.textContent = message;
            errorAlert.classList.remove('hidden');
        }

        function hideError() {
            document.getElementById('errorAlert').classList.add('hidden');
        }