export function loadData(hourlyPlayerCount, dailyPlayerCount) {
    const plugin = {
        id: 'verticalLiner',
        afterInit: (chart, args, opts) => {
            chart.verticalLiner = {}
        },
        afterEvent: (chart, args, options) => {
            const { inChartArea } = args
            chart.verticalLiner = { draw: inChartArea }
        },
        beforeTooltipDraw: (chart, args, options) => {
            const { draw } = chart.verticalLiner
            if (!draw) return

            const { ctx } = chart
            const { top, bottom } = chart.chartArea
            const { tooltip } = args
            const x = tooltip?.caretX
            if (!x) return

            ctx.save()

            ctx.beginPath()
            ctx.moveTo(x, top)
            ctx.lineTo(x, bottom)
            ctx.stroke()

            ctx.restore()
        }
    }

    const gradient = window['chartjs-plugin-gradient'];
    Chart.register(gradient);

    // Tworzenie wykresu za pomocą Chart.js
    createOnlinePlayerChart('online-player-chart', hourlyPlayerCount.slice(-48), plugin, true, false);
    createOnlinePlayerChart('daily-player-chart-7', hourlyPlayerCount, plugin, true, false);
    createOnlinePlayerChart('daily-player-chart-30', dailyPlayerCount.slice(-30), plugin, false, true);
    createOnlinePlayerChart('daily-player-chart-365', dailyPlayerCount.slice(-365), plugin, false, true);

    createOnlinePlayerChart('online-player-chart-down-panel', hourlyPlayerCount.slice(-48), plugin, true, false);
}

function createOnlinePlayerChart(id, data, plugin, beginAtZero, labelsWithDay) {
    var labels;

    if(labelsWithDay){
        labels = data.map(function (entry) {
            var date = new Date(entry.time);
        
            var time = ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
            
            var day = ('0' + date.getDate()).slice(-2);
            var month = ('0' + (date.getMonth() + 1)).slice(-2);
            var year = date.getFullYear();
            var formattedDate = day + '.' + month + '.' + year;
        
            return formattedDate + ' ' + time;
        });
    }
    else{
        labels = data.map(function (entry) {
            
            var date = new Date(entry.time);
            return ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
        });
    }

    var playerCounts = data.map(function (entry) {
        return entry.playerCount;
    });

    var minPlayerCount = Math.min(...playerCounts);

    var yConfig;

    if (beginAtZero || minPlayerCount < 10) {
        yConfig = {
            beginAtZero: true,
            ticks: {
                color: '#ffffff80'
            }
        };
    }
    else {
        yConfig = {
            min: minPlayerCount - 10,
            ticks: {
                color: '#ffffff80'
            }
        };
    }

    var ctx = document.getElementById(id).getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Online',
                data: playerCounts,
                backgroundColor: 'rgba(75, 192, 192, 1)',
                borderColor: 'rgba(0,0,0,0.0)',
                borderWidth: 1,
                fill: true,
                tension: 0.3,
                gradient: {
                    backgroundColor: {
                        axis: 'y',
                        colors: {
                            0: 'rgba(226,162,88,1)',
                            100: 'rgba(236,196,103,1)'
                        }
                    }
                }
            }]
        },
        options: {
            interaction: {
                mode: 'index',
                intersect: false,
            },
            scales: {
                x: {
                    ticks: {
                        color: '#ffffff80'
                    }
                },
                y: yConfig
            },
            plugins: {
                legend: {
                    display: false
                }
            },
            animation: {
                duration: 0
            },
            radius: 0,
        },
        plugins: [plugin]
    });
}
