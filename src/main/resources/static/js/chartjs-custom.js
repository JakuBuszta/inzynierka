const ctx = document.getElementById('myChart');
var valueInTime = historicalData.map(x => x["dataValue"].toFixed(2))
var valuePlacedAt = historicalData.map(x => x["placedAt"])

var profitLoss = historicalData.map(x => x["profitLoss"])

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
                type: 'linear',
                display: true,
                position: 'left',
            },
            x: {
                display: true
            }
        }
    }
});



document.addEventListener("DOMContentLoaded", function() {
    var buttons = document.querySelectorAll(".accordion-toggle");

    buttons.forEach(function(button, index) {
        button.addEventListener("click", function() {

            var targetId = button.getAttribute("data-bs-target");
            var target = document.getElementById(targetId);


            const cryptoId = targetId.replace("accordion-", "");
            const apiUrl = '/portfolio/' + cryptoId;

            fetch(apiUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error('Błąd sieci: ' + response.status);
                    }
                    return response.json();
                })
                .then((data) => {
                    var ctx = document.getElementById("chart-" + cryptoId);
                    var valueInTime = data.map(x => x["dataValue"])
                    var valuePlacedAt = data.map(x => x["placedAt"])
                    var profitLoss = data.map(x => x["profitLoss"])

                    new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: valuePlacedAt,
                            datasets: [{
                                label: 'Value (' + cryptoId + ')',
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
                                    type: 'linear',
                                    display: true,
                                    position: 'left',
                                },
                                x: {
                                    display: true
                                }
                            }
                        }
                    });
                })
                .catch((error) => {
                    console.error('Wystąpił błąd:', error);
                });


            if (target.classList.contains("show")) {
                target.classList.remove("show");
            } else {
                target.classList.add("show");
            }
        });
    });
});
