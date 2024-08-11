const url = "ws://localhost:9902/spring-boot-websocket";
const topic1 = "/topic/all";
const topic2 = "/topic/reply";
const REMOTE_URL = 'http://localhost:9902';
const REMOTE_URL_9001 = 'http://localhost:9901';

var drawLinechartSymbol = null;

const $ = (id) => document.getElementById(id);

const handleEvent = async (event, className, callback) => {
    if (event.target.classList.contains(className)) {
		switch(className) {
			case 'draw-linechart': // 更新線圖
				var symbol = event.target.getAttribute('data-id');
				await callback(symbol);
				break;
		}
    }
};

// 更新部位
const refreshStocks = async () => {
	const fullUrl = `${REMOTE_URL}/orders`;
	const response = await fetch(fullUrl);
	const stocks = await response.json();
	console.log(stocks);
	// 更新部位
	displayStocks(stocks);
};

const displayStocks = (stocks) => {
	const stockBody = $('stockBody');
	stockBody.innerHTML = '';
	stocks.forEach((stock) => {
		const tr = document.createElement('tr');
		// tr 加入 data-id 屬性，方便後續取得 symbol
		tr.setAttribute('data-id', stock.symbol);
		stock.avgPrice = formatPrice(stock.avgPrice, 2);
		tr.innerHTML = `
            <td>${stock.symbol}</td>
            <td>${stock.avgPrice}</td>
            <td>${stock.amount}</td>
            <td>${stock.profit}</td>
        `;
		stockBody.appendChild(tr);
	});
};

// 更新線圖與五檔
const refreshLinechartAndFivePrice = (symbol) => {
    drawLinechartSymbol = null;
    const fullUrl = `${REMOTE_URL}/pre_price/${symbol}`;

    fetch(fullUrl)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            // 重新繪製圖表
            refreshChart(data);

            // 回補線圖
            const fullUrl_9001 = `${REMOTE_URL_9001}/price/hhmm00/${symbol}`;
            return fetch(fullUrl_9001); // 回傳下一個 fetch 的 Promise
        })
        .then(response_9001 => response_9001.json())
        .then(quotes => {
            console.log(quotes.length);
            quotes.forEach((quote) => {
                setTimeout(() => {
                    quote = JSON.parse(quote);
                    drawStockLineChart(symbol, quote);
                }, 10); // 延遲 10 毫秒
            });

            // 清除五檔
            clearFivePriceTable();

            // 取得最新報價
            const fullUrl_Five = `${REMOTE_URL_9001}/price/last/${symbol}`;
            return fetch(fullUrl_Five); // 回傳最後一個 fetch 的 Promise
        })
        .then(response_Five => response_Five.json())
        .then(quote => {
            console.log(quote);
            // 繪製五檔
            console.log('繪製五檔');
            drawFivePriceTable(symbol, quote);
			// 更新下單表單
			console.log('更新下單表單');
			inputSubmotOrderForm(quote);
            // 全域變數設定給 ws 使用
            drawLinechartSymbol = symbol;
        })
        .catch(error => console.error('錯誤:', error)); // 處理任何一步中的錯誤
};


const formatPrice = (price, digit) => {
    const formatter = new Intl.NumberFormat('en-US', {
        style: 'decimal', // 使用十进制数字格式
        minimumFractionDigits: digit, // 保持小数点后 n 位
        maximumFractionDigits: digit, // 最多小数点后 n 位
    });

    return formatter.format(parseFloat(price));
};

const client = new StompJs.Client({
    brokerURL: url
});

var subscriptions = {}; // 存儲訂閱的物件

client.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    client.subscribe(topic1, (message) => {
		//console.log('收到消息: ' + message.body);
    });
    
    client.subscribe(topic2, (message) => {
		var result = JSON.parse(message.body);
		console.log('收到回報: ' + message.body);
		// 更新交易回報
		updateReturnBody(result);
		// 更新部位
		if(result.status == 'true' || result.status == true) {
			// 延遲 0.1 秒後更新部位
			setTimeout(() => {
				refreshStocks();
			}, 100);
		}
    });
    
};

client.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

client.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

const submitSubscribe = async () => {
	const symbol = symbolInput.value;
	
	if (symbol == '') {
		alert('請輸入股票代號');
		return;
	}
	
	subscribe(symbol);
}

const subscribe = async (symbol) => {
	const topic = "/topic/" + symbol;
	
	
	symbolInput.value = '';
	
	if (subscriptions[symbol]) {
		//alert(symbol + '已經訂閱過了');
		return;
	}
	    
	// 利用 fetch await 先透過 http://localhost:9902/price/{symbol} 取得最新的 quote
	const fullUrl = `${REMOTE_URL}/price/${symbol}`;
	const response = await fetch(fullUrl);
	if(response != null) {
		var quote = await response.json();
		displayQuote(symbol, quote);
		console.log(quote);
	}
	
	client.subscribe(topic, (message) => {
	    displayQuote(symbol, JSON.parse(message.body));
	});

	subscriptions[symbol] = true;
};

// 繪製線圖
function drawStockLineChart(symbol, quote) {
	var matchTime = quote.matchTime;
	matchTime = matchTime.slice(0, 6);
	drawLine(moment(matchTime, 'HHmmss'), quote.lastPrice);
}

// 繪製五檔
function drawFivePriceTable(symbol, quote) {
	$('five_buy_price_1').innerHTML = formatPrice(quote.bidPrices[0], 2);
	$('five_buy_price_2').innerHTML = formatPrice(quote.bidPrices[1], 2);
	$('five_buy_price_3').innerHTML = formatPrice(quote.bidPrices[2], 2);
	$('five_buy_price_4').innerHTML = formatPrice(quote.bidPrices[3], 2);
	$('five_buy_price_5').innerHTML = formatPrice(quote.bidPrices[4], 2);
	
	$('five_buy_amount_1').innerHTML = formatPrice(quote.bidVolumes[0], 0);
	$('five_buy_amount_2').innerHTML = formatPrice(quote.bidVolumes[1], 0);
	$('five_buy_amount_3').innerHTML = formatPrice(quote.bidVolumes[2], 0);
	$('five_buy_amount_4').innerHTML = formatPrice(quote.bidVolumes[3], 0);
	$('five_buy_amount_5').innerHTML = formatPrice(quote.bidVolumes[4], 0);
	
	$('five_sell_price_1').innerHTML = formatPrice(quote.askPrices[0], 2);
	$('five_sell_price_2').innerHTML = formatPrice(quote.askPrices[1], 2);
	$('five_sell_price_3').innerHTML = formatPrice(quote.askPrices[2], 2);
	$('five_sell_price_4').innerHTML = formatPrice(quote.askPrices[3], 2);
	$('five_sell_price_5').innerHTML = formatPrice(quote.askPrices[4], 2);
	
	$('five_sell_amount_1').innerHTML = formatPrice(quote.askVolumes[0], 0);
	$('five_sell_amount_2').innerHTML = formatPrice(quote.askVolumes[1], 0);
	$('five_sell_amount_3').innerHTML = formatPrice(quote.askVolumes[2], 0);
	$('five_sell_amount_4').innerHTML = formatPrice(quote.askVolumes[3], 0);
	$('five_sell_amount_5').innerHTML = formatPrice(quote.askVolumes[4], 0);
	
}

// 五檔清空
function clearFivePriceTable() {
	$('five_buy_price_1').innerHTML = '&nbsp;';
	$('five_buy_price_2').innerHTML = '&nbsp;';
	$('five_buy_price_3').innerHTML = '&nbsp;';
	$('five_buy_price_4').innerHTML = '&nbsp;';
	$('five_buy_price_5').innerHTML = '&nbsp;';
	
	$('five_buy_amount_1').innerHTML = '&nbsp;';
	$('five_buy_amount_2').innerHTML = '&nbsp;';
	$('five_buy_amount_3').innerHTML = '&nbsp;';
	$('five_buy_amount_4').innerHTML = '&nbsp;';
	$('five_buy_amount_5').innerHTML = '&nbsp;';
	
	$('five_sell_price_1').innerHTML = '&nbsp;';
	$('five_sell_price_2').innerHTML = '&nbsp;';
	$('five_sell_price_3').innerHTML = '&nbsp;';
	$('five_sell_price_4').innerHTML = '&nbsp;';
	$('five_sell_price_5').innerHTML = '&nbsp;';
	
	$('five_sell_amount_1').innerHTML = '&nbsp;';
	$('five_sell_amount_2').innerHTML = '&nbsp;';
	$('five_sell_amount_3').innerHTML = '&nbsp;';
	$('five_sell_amount_4').innerHTML = '&nbsp;';
	$('five_sell_amount_5').innerHTML = '&nbsp;';
	
}

function displayQuote (symbol, quote) {
    // 檢查是否已存在該 ID 的 div
    var existingId = document.getElementById(symbol);
    
    // 格式化時間 HH:mm:ss
    quote.matchTime = quote.matchTime.substring(0, 2) + ':' + quote.matchTime.substring(2, 4) + ':' + quote.matchTime.substring(4, 6);
    
    if (quote.lastPrice != null) {
	    // 繪製線圖與五檔
	    if(symbol != null && symbol == drawLinechartSymbol) {
	    	drawStockLineChart(symbol, quote);
	    	drawFivePriceTable(symbol, quote);
	    }
	    // 格式化價格
		quote.lastPrice = formatPrice(quote.lastPrice, 2);
	    
	}
	
	if (quote.lastVolume != null) {
	    quote.lastVolume = formatPrice(quote.lastVolume, 0);
	}
	
	if (quote.bidPrices[0] != null) {
	    quote.bidPrices[0] = formatPrice(quote.bidPrices[0], 2);
	}
	
	if (quote.bidVolumes[0] != null) {
	    quote.bidVolumes[0] = formatPrice(quote.bidVolumes[0], 0);
	}
	
	if (quote.askPrices[0] != null) {
	    quote.askPrices[0] = formatPrice(quote.askPrices[0], 2);
	}
	
	if (quote.askVolumes[0] != null) {
	    quote.askVolumes[0] = formatPrice(quote.askVolumes[0], 0);
	}

    if (existingId) {
        // 如果 id 已存在，更新內容
        // 若 quote.lastPrice = null 則該欄位不更新
        existingId.cells[0].innerHTML = quote.symbol;
        if (quote.lastPrice != null) {
			existingId.cells[1].innerHTML = quote.lastPrice;
		}
		if (quote.lastVolume != null) {
			existingId.cells[2].innerHTML = quote.lastVolume;
		}
		existingId.cells[3].innerHTML = quote.bidPrices[0];
        existingId.cells[4].innerHTML = quote.bidVolumes[0];
        existingId.cells[5].innerHTML = quote.askPrices[0];
        existingId.cells[6].innerHTML = quote.askVolumes[0];
        existingId.cells[7].innerHTML = quote.matchTime;
        //existingId.innerHTML = '<td>' + quote.symbol + '</td><td>' + quote.lastPrice + '</td><td>' + quote.lastVolume + '</td><td>' + quote.bidPrices[0] + '</td><td>' + quote.bidVolumes[0] + '</td><td>' + quote.askPrices[0] + '</td><td>' + quote.askVolumes[0] + '</td><td>' + quote.matchTime + '</td>';
    	
    	// 使背景閃爍
        existingId.classList.add('flash');
		existingId.addEventListener('animationend', () => {
            existingId.classList.remove('flash');
        });
    } else {
        // 如果 id 不存在，創建一個新的 tr
        var tr = document.createElement('tr');
        tr.id = symbol; // 設定 id
        
        tr.innerHTML = `
		    <td align="center" class="draw-linechart" data-id="${quote.symbol}">${quote.symbol}</td>
		    <td align="right">${quote.lastPrice}</td>
		    <td align="right">${quote.lastVolume}</td>
		    <td align="right">${quote.bidPrices[0]}</td>
		    <td align="right">${quote.bidVolumes[0]}</td>
		    <td align="right">${quote.askPrices[0]}</td>
		    <td align="right">${quote.askVolumes[0]}</td>
		    <td align="center">${quote.matchTime}</td>
		`;
		
		
        
        receiveBody.appendChild(tr); 
        // 使背景閃爍
        tr.classList.add('flash');
        tr.addEventListener('animationend', () => {
            tr.classList.remove('flash');
        });
    }
    
    // 更新庫存部位損益
    const stockBody = $('stockBody');
    const stockRows = stockBody.querySelectorAll('tr');
    // 找到 symbol 對應的 tr
    const stockRow = Array.from(stockRows).find((row) => row.getAttribute('data-id') === symbol);
	if (stockRow) {
		const avgPrice = parseFloat(stockRow.cells[1].innerHTML);
		const amount = parseFloat(stockRow.cells[2].innerHTML);
		const profit = (quote.lastPrice - avgPrice) * amount;
		stockRow.cells[3].innerHTML = formatPrice(profit, 2);
	}
    
}

// 更新下單表單
const inputSubmotOrderForm = (quote) => {
	$('orderSymbolInput').value = quote.symbol;
	$('orderPriceInput').value = quote.lastPrice;
	$('orderAmountInput').value = 1;
};

// 下單
const submitOrder = async() => {
	const symbol = $('orderSymbolInput').value;
	const buy = $('orderBuyRadio').checked;
	const sell = $('orderSellRadio').checked;
	const price = $('orderPriceInput').value;
	const amount = $('orderAmountInput').value;
	
	const order = {
		symbol: symbol,
		bs: buy ? 'B' : sell ? 'S' : null,
		price: price,
		amount: amount
	};
	
	console.log(order);
	// POST http://localhost:9902/order
	const fullUrl = `${REMOTE_URL}/order`;
	const response = await fetch(fullUrl, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(order)
	});
	const result = await response.json();
	console.log(result);
};

// 更新下單交易回報
const updateReturnBody = (result) => {
	const tr = document.createElement('tr');
	tr.innerHTML = `
		<td>${result.orderNo}</td>
		<td>${result.status}</td>
        <td>${result.symbol}</td>
        <td>${result.bs}</td>
        <td>${result.price}</td>
        <td>${result.amount}</td>
        <td>${result.matchPrice}</td>
        <td>${result.matchAmount}</td>
    `;

	$('returnBody').appendChild(tr);
};

// 網頁載入完成後執行
document.addEventListener("DOMContentLoaded", function() {
	client.activate();
	console.log('WebSocket client activated');
	
	const symbolInput = $('symbolInput');
	const subscribeBtn = $('subscribeBtn');
	const form = $('myForm');
	const receiveBody = $('receiveBody');
	const orderSubmitBtn = $('orderSubmitBtn');
	
	// 載入部位
	refreshStocks();
	
	// 防止表单提交
	form.addEventListener('submit', function(event) {
	    event.preventDefault();
	});
	
	subscribeBtn.addEventListener('click', function () {
		event.preventDefault();
		submitSubscribe();
	});
	
	// symbolInput 按下 enter 時，觸發 subscribe
	symbolInput.addEventListener('keyup', function (event) {
		if (event.keyCode === 13) {
			event.preventDefault();
			submitSubscribe();
		}
	});
	
	receiveBody.addEventListener("click", async (event) => {
		event.preventDefault();  // 取消默認動作，這裡是阻止超鏈接跳轉
		console.log(event.target);
		await handleEvent(event, 'draw-linechart', refreshLinechartAndFivePrice);
		
	});
	
	// 下單
	orderSubmitBtn.addEventListener('click', function() {
		event.preventDefault();
		submitOrder();
	});
	
	// 預設訂閱 2330, 2303, 3008, 2317, 0050, 1101, 1402, 00632R
	//const defaultSymbols = ['2330', '2303', '3008', '2317', '0050', '1101', '1402', '00632R', '2884', '2891', '3231'];
	//const defaultSymbols = ['0056', '2344', '4438', '2330', '2303', '1413', '0050', '3008', '1538', '8101'];
	const defaultSymbols = ['2344', '2330', '0050', '3008'];
	defaultSymbols.forEach((symbol) => {
		//  避免訂閱太快，導致後端還沒訂閱完成
		setTimeout(() => {
			subscribe(symbol);
		}, 50);
	});
	
	
	
});

