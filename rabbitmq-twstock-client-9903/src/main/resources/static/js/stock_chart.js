const ctx = document.getElementById('myChart').getContext('2d');
const timelabels = ['0900', '0930', '1000', '1030', '1100', '1130', '1200', '1230', '1300', '1330'];
var myChart = null

// 設置初始時間 0900
let currentTime = moment('0900', 'HHmm');

// 設置結束時間 1330
const endTime = moment('1330', 'HHmm');

// 定義一個變數來儲存定時器的 ID，以便稍後可以清除定時器
let intervalId;

const drawLine = (ts, value) => {
	// 建立新的數據點
	let newDataPoint = {
		x: ts.format('HH:mm'), // 使用 moment 格式化時間
		y: value
	};
	
	// 將新的資料點新增至圖表的資料集中
	myChart.data.datasets[0].data.push(newDataPoint);
	
	// 更新所有點的 pointRadius 為 0
    myChart.data.datasets[0].pointRadius = myChart.data.datasets[0].data.map(() => 0);

    // 設定最後一個點的 pointRadius 為 5
    if (myChart.data.datasets[0].data.length > 0) {
        myChart.data.datasets[0].pointRadius[myChart.data.datasets[0].data.length - 1] = 5;
    }
    
	// 更新圖表以顯示新的數據點
	myChart.update();
};

// 重新繪製圖表
const refreshChart = (data) => {
	if (data == null) {
		return;
	}
	if (myChart != null) {
		myChart.destroy();
	}
	
	const symbol = data['symbol'];
	const name = data['name'];
    const closePrice = data['closePrice'] * 1;
	
	myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: timelabels,
        datasets: [{
            label: `${symbol} ${name} 股價走勢圖`,
            data: [
                
            ],
            fill: false,
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1,
            pointRadius: 0 // 設定資料點的半徑為 0，即不顯示資料點
        }]
    },
	options: {
	    scales: {
	        xAxes: [{
	            type: 'time',
	            time: {
	                parser: 'HHmm',
	                unit: 'minute',
	                tooltipFormat: 'HHmm'
	            },
	            distribution: 'linear',
	            ticks: {
	                // 使用回調函數自訂刻度標籤
	                callback: function(value, index, values) {
	                    // 仅显示指定的时间点作为 X 轴标签
	                    const specifiedLabels = timelabels;
	                    const labelMoment = moment(value, 'HHmm');
	                    const label = labelMoment.format('HHmm');
	                    if (specifiedLabels.includes(label)) {
	                        return label;
	                    }
	                    return null;  // 不顯示其他時間點的標籤
	                },
	                maxRotation: 0,  // 防止標籤旋轉
	                autoSkip: false,  // 停用自動跳過，確保所有指定的標籤都顯示
	            }
	        }],
	        yAxes: [{
	            ticks: {
	                beginAtZero: false, // 不從0開始
	                //responsive: true, // 符合響應式
	                max: closePrice*1.1, // 設置Y軸最大值
	                min: closePrice*0.9, // 設置Y軸最小值
	                // 自訂刻度間隔，例如每隔5個單位顯示一個刻度標籤
	                stepSize: closePrice>1000?50:closePrice>500?10:closePrice>100?5:closePrice>50?1:closePrice>10?0.5:0.1
	            }
	        }]
	    }
	}});
};
