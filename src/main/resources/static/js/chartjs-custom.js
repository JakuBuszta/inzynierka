const ctx = document.getElementById('myChart');
var valueInTime = historicalData.map(x => x["dataValue"].toFixed(2))
var valuePlacedAt = historicalData.map(x => x["placedAt"])


var profitLoss = historicalData.map(x => x["profitLoss"])

console.log(valueInTime);

const myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: valuePlacedAt,
        datasets: [{
            label: 'Portfolio value',
            data: valueInTime,
            borderColor: [
                'rgba(0, 156, 255, 1)'
            ],
            backgroundColor: ['rgba(0, 156, 255, 1)'],
            borderWidth: 1
        },
            {
                label: "Profit/Loss",
                data: profitLoss,
                borderColor: ["rgba(70, 255, 77, 1)"],
                backgroundColor: ["rgba(70, 255, 77, 1)"],
                borderWidth: 1
            }
        ]
    },

    options: {
        maintainAspectRatio: false,
        scales: {
            y: {
                stacked: true,
                grid: {
                    display: true,
                    color: "rgba(255,99,132,0.2)"
                }
            },
            x: {
                grid: {
                    display: false
                }
            }
        }
    }
});


//(70, 255, 77)

