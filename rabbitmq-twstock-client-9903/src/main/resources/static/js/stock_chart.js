const ctx = document.getElementById('myChart').getContext('2d');
const timelabels = ['0900', '0930', '1000', '1030', '1100', '1130', '1200', '1230', '1300', '1330'];
var myChart = null

// 设置初始时间为 0900
let currentTime = moment('0900', 'HHmm');

// 设置结束时间为 1330
const endTime = moment('1330', 'HHmm');

// 定义一个变量来存储定时器的 ID，以便稍后可以清除定时器
let intervalId;

const drawLine = (ts, value) => {
	// 创建新的数据点
	let newDataPoint = {
		x: ts.format('HH:mm'), // 使用 moment 格式化时间
		y: value
	};
	
	// 将新的数据点添加到图表的数据集中
	myChart.data.datasets[0].data.push(newDataPoint);
	
	// 更新所有点的 pointRadius 为 0
    myChart.data.datasets[0].pointRadius = myChart.data.datasets[0].data.map(() => 0);

    // 设置最后一个点的 pointRadius 为 5
    if (myChart.data.datasets[0].data.length > 0) {
        myChart.data.datasets[0].pointRadius[myChart.data.datasets[0].data.length - 1] = 5;
    }
    
	// 更新图表以显示新的数据点
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
            pointRadius: 0 // 设置数据点的半径为 0，即不显示数据点
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
	                // 使用回调函数自定义刻度标签
	                callback: function(value, index, values) {
	                    // 仅显示指定的时间点作为 X 轴标签
	                    const specifiedLabels = timelabels;
	                    const labelMoment = moment(value, 'HHmm');
	                    const label = labelMoment.format('HHmm');
	                    if (specifiedLabels.includes(label)) {
	                        return label;
	                    }
	                    return null;  // 不显示其他时间点的标签
	                },
	                maxRotation: 0,  // 防止标签旋转
	                autoSkip: false,  // 禁用自动跳过，确保所有指定的标签都显示
	            }
	        }],
	        yAxes: [{
	            ticks: {
	                beginAtZero: false, // 不从0开始
	                //responsive: true, //符合響應式
	                max: closePrice*1.1, // 设置Y轴最大值
	                min: closePrice*0.9, // 设置Y轴最小值
	                // 自定义刻度间隔，例如每隔5个单位显示一个刻度标签
	                stepSize: closePrice>1000?50:closePrice>500?10:closePrice>100?5:closePrice>50?1:closePrice>10?0.5:0.1
	            }
	        }]
	    }
	}});
};
