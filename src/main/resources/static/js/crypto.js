// Email Alerts
            function openEmailPrefs() {
    			document.getElementById('emailPrefsModal').classList.remove('hidden');
    				loadEmailPreferences();
}

function closeEmailPrefs() {
    document.getElementById('emailPrefsModal').classList.add('hidden');
}

async function loadEmailPreferences() {
    try {
        const response = await fetch('/api/email-preferences', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        });
        if (response.ok) {
            const prefs = await response.json();
            document.getElementById('enableReport').checked = prefs.dailyReportEnabled;
            document.getElementById('reportTime').value = prefs.reportTime;
            document.getElementById('reportFormat').value = prefs.reportFormat;
        }
    } catch (error) {
        console.error('Error loading email preferences:', error);
    }
}

async function sendTestReport() {
    try {
        const response = await fetch('/api/email-preferences/test', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        });
        if (response.ok) {
            alert('Test report sent! Please check your email.');
        } else {
            alert('Failed to send test report. Please try again.');
        }
    } catch (error) {
        console.error('Error sending test report:', error);
        alert('Error sending test report');
    }
}

document.getElementById('emailPrefsForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const data = {
        dailyReportEnabled: document.getElementById('enableReport').checked,
        reportTime: document.getElementById('reportTime').value,
        reportFormat: document.getElementById('reportFormat').value
    };

    try {
        const response = await fetch('/api/email-preferences', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            alert('Email preferences saved successfully!');
            closeEmailPrefs();
        } else {
            alert('Failed to save preferences. Please try again.');
        }
    } catch (error) {
        console.error('Error saving email preferences:', error);
        alert('Error saving preferences');
    }
});
            // Email Alerts END


        let selectedCryptos = new Set();
        let availableCryptos = [];

        document.addEventListener('DOMContentLoaded', async function() {
            const token = localStorage.getItem('token');
            const userEmail = localStorage.getItem('userEmail');
            
            if (token) {
                setupAuthenticatedView(userEmail);
                await loadUserCryptos();
            } else {
                setupUnauthenticatedView();
                await fetchAndDisplayCryptos();
            }
        });

        function setupAuthenticatedView(userEmail) {
            document.getElementById('userGreeting').classList.remove('hidden');
            document.getElementById('userName').textContent = userEmail;
            document.getElementById('demo_acc').classList.add('hidden');
            
            
            document.getElementById('heroSection').innerHTML = `
                <button onclick="openCryptoSelector()" 
                        class="inline-block px-6 py-3 bg-green-600 text-white font-semibold rounded-lg hover:bg-green-700 transition-colors">
                    Add or Edit my Crypto
                </button>
            `;
        }

        function setupUnauthenticatedView() {
            document.getElementById('userGreeting').classList.add('hidden');
            
            document.getElementById('authButtons').innerHTML = `
                <a href="/login.html" class="px-4 py-2 text-green-600 hover:text-green-800">Sign in</a>
            `;
            
            document.getElementById('heroSection').innerHTML = `
                <p class="text-xl text-gray-600 mb-6">
                    Register and choose your own crypto list. Setup alerts and more.
                </p>
                <a href="/register.html" 
                   class="inline-block px-6 py-3 bg-green-600 text-white font-semibold rounded-lg hover:bg-green-700 transition-colors">
                    Register Now
                </a>
            `;
        }

        async function loadUserCryptos() {
            try {
                const response = await fetch('/api/crypto/user-selection', {
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token')
                    }
                });
                if (response.ok) {
                    const cryptos = await response.json();
                    displayCryptos(cryptos);
                    selectedCryptos = new Set(cryptos.map(crypto => crypto.id.toLowerCase()));
                }
            } catch (error) {
                console.error('Error loading user cryptos:', error);
            }
        }

        async function fetchAndDisplayCryptos() {
            try {
                const response = await fetch('/api/crypto/top-three');
                const cryptos = await response.json();
                displayCryptos(cryptos);
            } catch (error) {
                console.error('Error fetching crypto data:', error);
            }
        }


function displayCryptos(cryptos) {
   const container = document.getElementById('cryptoGrid');
   container.innerHTML = cryptos.map(crypto => {
       const priceChange = crypto.price_change_percentage_24h || 0;
       const isPositive = priceChange > 0;
       const changeColor = isPositive ? 'text-green-600' : priceChange < 0 ? 'text-red-600' : 'text-gray-600';
       const bgColorClass = isPositive ? 'hover:bg-green-50' : priceChange < 0 ? 'hover:bg-red-50' : 'hover:bg-gray-50';
       
       return `
           <div class="bg-white rounded-lg shadow p-4 sm:p-6 transition-all duration-300 ${bgColorClass}"
                data-symbol="${crypto.id}">
               <div class="flex justify-between items-center mb-2">
                   <h3 class="text-lg font-bold">${crypto.id.toUpperCase()}</h3>
                   <div class="text-sm text-gray-500">${crypto.name}</div>
               </div>
               <div class="flex justify-between items-center">
                   <div class="text-xl font-bold ${changeColor} transition-colors duration-300"
                        data-price="${crypto.current_price}">
                       $${formatNumber(crypto.current_price)}
                   </div>
                   <div class="text-sm font-medium ${changeColor}">
                       ${priceChange > 0 ? '+' : ''}${priceChange.toFixed(2)}%
                   </div>
               </div>
           </div>
       `;
   }).join('');
}



        async function openCryptoSelector() {
            document.getElementById('cryptoModal').classList.remove('hidden');
            await loadAvailableCryptos();
        }


        async function loadAvailableCryptos() {
    try {
        const response = await fetch('/api/crypto/available', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        });
        if (response.ok) {
            availableCryptos = await response.json();
            displayAvailableCryptos(availableCryptos);
            updateSelectedDisplay();
        }
    } catch (error) {
        console.error('Error loading available cryptos:', error);
    }
}


        function displayAvailableCryptos(cryptos) {
    const container = document.getElementById('availableCryptos');
    const cryptoArray = Array.isArray(cryptos) ? cryptos : [cryptos];

    container.innerHTML = cryptoArray.map(crypto => `
        <div class="flex items-center p-2 hover:bg-gray-100 rounded">
            <input type="checkbox" 
                   id="crypto-${crypto.id.toLowerCase()}" 
                   value="${crypto.id.toLowerCase()}"
                   ${selectedCryptos.has(crypto.id.toLowerCase()) ? 'checked' : ''}
                   onchange="toggleCrypto('${crypto.id.toLowerCase()}', '${crypto.name}')"
                   class="mr-2">
            <label for="crypto-${crypto.id.toLowerCase()}" class="flex-grow cursor-pointer">
                ${crypto.name} (${crypto.id.toUpperCase()})
            </label>
        </div>
    `).join('');
}



        function toggleCrypto(id, name) {
            if (selectedCryptos.has(id)) {
                selectedCryptos.delete(id);
            } else if (selectedCryptos.size < 15) {
                selectedCryptos.add(id);
            } else {
                alert('You can select maximum 15 cryptocurrencies');
                document.getElementById(`crypto-${id}`).checked = false;
                return;
            }
            updateSelectedDisplay();
        }

        function updateSelectedDisplay() {
            const container = document.getElementById('selectedCryptos');
            const count = document.getElementById('selectedCount');
            count.textContent = selectedCryptos.size;
            
            container.innerHTML = Array.from(selectedCryptos).map(id => {
                const crypto = availableCryptos.find(c => c.id.toLowerCase() === id);
                return crypto ? `
                    <span class="inline-flex items-center px-2 py-1 bg-green-100 text-green-800 rounded">
                        ${crypto.id.toUpperCase()}
                        <button onclick="toggleCrypto('${id}')" class="ml-1 hover:text-red-600">&times;</button>
                    </span>
                ` : '';
            }).join('');
        }

        async function saveCryptoSelection() {
            try {
                const response = await fetch('/api/crypto/user-selection', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token'),
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(Array.from(selectedCryptos))
                });
                
                if (response.ok) {
                    closeCryptoSelector();
                    await loadUserCryptos();
                } else {
                    const error = await response.json();
                    alert(error.message || 'Failed to save your selection. Please try again.');
                }
            } catch (error) {
                console.error('Error saving selection:', error);
                alert('Failed to save your selection. Please try again.');
            }
        }

        function closeCryptoSelector() {
            document.getElementById('cryptoModal').classList.add('hidden');
        }

        function logout() {
            fetch('/api/logout', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            }).finally(() => {
                localStorage.removeItem('token');
                localStorage.removeItem('userEmail');
                window.location.reload();
            });
        }

        function formatNumber(num) {
            if (num === null || num === undefined) return '0.00';
            return new Intl.NumberFormat('en-US', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }).format(num);
        }

        // Search functionality
        document.getElementById('cryptoSearch').addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            const filtered = availableCryptos.filter(crypto => 
                crypto.name.toLowerCase().includes(searchTerm) || 
                crypto.id.toLowerCase().includes(searchTerm)
            );
            displayAvailableCryptos(filtered);
        });