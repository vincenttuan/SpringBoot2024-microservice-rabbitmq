/**
 * 檢查給定的 token 是否是有效的 JWT 格式
 * @param {string} token - JWT token
 * @returns {boolean} - 如果是有效的 JWT 格式，則返回 true，否則返回 false
 */
function isJWT(token) {
    const parts = token.split('.');
    if (parts.length !== 3) {
        return false;
    }

    try {
        // 將 Base64Url 編碼轉換為 Base64 編碼：
        // Base64Url 使用 - 替換 +，使用 _ 替換 /，並且省略結尾的 = 符號。
        const header = atob(parts[0].replace(/-/g, '+').replace(/_/g, '/'));
        const payload = atob(parts[1].replace(/-/g, '+').replace(/_/g, '/'));
        
        // 確保是 JSON 格式
        JSON.parse(header);
        JSON.parse(payload);
    } catch (e) {
        return false;
    }

    return true;
}

// 簡單的函數來獲取 cookies 中的值
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}


// 綁定獲取 token 按鈕的點擊事件
document.getElementById('fetchTokenButton').addEventListener('click', async () => {
    try {
        // 發送請求以獲取 JWT Token
        const response = await fetch('http://localhost:7001/jwt', {
            method: 'GET', // 使用 GET 方法
            headers: {
                'User-Agent': 'john', // 替換為實際的用戶名
                'Service-Identifier': 'report', // 替換為實際的服務 ID
                'Authorization': 'Basic ' + btoa('john:1234') // 替換為實際的用戶名和密碼
            }
        });

        if (response.ok) {
            const token = await response.text();
            // 簡單判斷 token 是否是 JWT 格式
            if (!isJWT(token)) {
                alert('取得的資料不是 JWT Token 格式, 請確認 username 或 password 是否正確.');
                return;
            }

            console.log('令牌:', token);
            // 將 JWT Token 存儲到 Cookies 中，讓 cookies 可以跨域存取
            document.cookie = `jwt=${token}; path=/; SameSite=None; Secure`;
            console.log('JWT 令牌已存儲到 Cookies 中。');
        } else {
            console.log('獲取 JWT 令牌失敗:', response);
        }
    } catch (error) {
        console.error('獲取 JWT 令牌時出錯:', error);
    }
});

document.getElementById('verifyTokenButton').addEventListener('click', async () => {
    try {
        // 從 cookies 中讀取 JWT token
        const token = getCookie('jwt');
        if (!token) {
            console.log(`JWT token 不存在，請先獲取 token。`);
            alert('JWT token 不存在，請先獲取 token。');
            return;
        }

        // 發送請求以驗證 JWT Token
        const response = await fetch('http://localhost:7001/verifyJwt', {
            method: 'GET', // 使用 GET 方法
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const message = await response.text();
            console.log(`JWT 令牌驗證成功: ${message}`);
            alert('JWT 令牌驗證成功: ' + message);
        } else {
			console.log(`JWT 令牌驗證失敗: ${response.status}`);
            alert('JWT 令牌驗證失敗: ' + response.status);
        }
    } catch (error) {
		console.log(`驗證 JWT 令牌時出錯: ${error}`)
        console.error('驗證 JWT 令牌時出錯: ', error);
    }
});

document.getElementById('printCookieButton').addEventListener('click', async () => {
	console.log('Cookies:', document.cookie==''?null:document.cookie);
});


document.getElementById('clearCookieButton').addEventListener('click', async () => {
	// 清除所有 jwt cookie
	document.cookie = 'jwt=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=None; Secure';
	console.log('Cookies 已清除。', 'Cookies:', document.cookie==''?null:document.cookie);
});

document.getElementById('fetchOrderButton').addEventListener('click', async () => {
    try {
        // 從 cookies 中讀取 JWT token
        const token = getCookie('jwt');
        if (!token) {
            console.log(`JWT token 不存在，請先獲取 token。`);
            alert('JWT token 不存在，請先獲取 token。');
            return;
        }

        // 發送請求以驗證 JWT Token
        const response = await fetch('http://localhost:7002/orders', {
            method: 'GET', // 使用 GET 方法
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const message = await response.text();
            console.log(`${message}`);
            alert(message);
        } else {
			console.log(`失敗: ${response.status}`);
            alert('失敗: ' + response.status);
        }
    } catch (error) {
		console.log(`錯誤: ${error}`)
        console.error('錯誤: ', error);
    }
});

document.getElementById('fetchProductButton').addEventListener('click', async () => {
    console.log('fetchProductButton 請自行練習');
    alert('fetchProductButton 請自行練習');
});

