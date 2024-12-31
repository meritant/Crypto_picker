document.getElementById('registerForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            hideError();
            
            const data = {
                name: document.getElementById('name').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value
            };

            try {
                const response = await fetch('/api/register', {
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
                    showError(error.message || 'Registration failed. Please try again.');
                }
            } catch (error) {
                console.error('Error:', error);
                showError('Error during registration. Please try again.');
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