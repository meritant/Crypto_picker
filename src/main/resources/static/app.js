const username = 'user';
const password = '123456';
const headers = new Headers();
headers.append('Authorization', 'Basic ' + btoa(username + ':' + password));
headers.append('Accept', 'application/json');

function fetchCryptos() {
   fetch('/api/crypto/top-prices', {
       method: 'GET',
       headers: headers,
       credentials: 'include'
   })
   .then(response => response.json())
   .then(data => displayCryptos(data))
   .catch(error => console.error('Error:', error));
}

function displayCryptos(cryptos) {
    const container = document.getElementById('crypto-container');
    container.innerHTML = cryptos.map(crypto => `
        <div class="crypto-card">
            <h2>${crypto.name} (${crypto.symbol.toUpperCase()})</h2>
            <p>Price: $${crypto.current_price}</p>
            <p>Market Cap: $${crypto.market_cap.toLocaleString()}</p>
        </div>
    `).join('');
 }

fetchCryptos();
setInterval(fetchCryptos, 60000);