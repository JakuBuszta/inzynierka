function saveCurrency(currency) {
    const data = {
        selectedCurrency: currency
    };
    localStorage.setItem('selectedCurrency', JSON.stringify(data));


    var contextPath = "[[@{/}]]";
    var csrfHeaderName = "[[${_csrf.headerName}]]";
    var csrfValue = "[[${_csrf.token}]]";
    var url = "/" + currency;

    console.log(currency)
    $.ajax({
        type: 'POST',
        url: url,
        // beforeSend: function (xhr){
        //     xhr.setRequestHeader(csrfHeaderName, csrfValue)
        // },
    }).done(function () {
        console.log("done")
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("fail", textStatus, errorThrown);
    })


    window.location.href = '/';
}


function getCurrency() {
    var currency = localStorage.getItem('selectedCurrency', JSON.stringify(data));
    if (currency == null) return "/"

    return currency;
}

function addToWatchList(coinName, icon) {
    var contextPath = "[[@{/}]]";
    var csrfHeaderName = "[[${_csrf.headerName}]]";
    var csrfValue = "[[${_csrf.token}]]";
    var url = "/" + "watchlist/" + coinName;

    $.ajax({
        type: 'POST',
        url: url,
        // beforeSend: function (xhr){
        //     xhr.setRequestHeader(csrfHeaderName, csrfValue)
        // },
    }).done(function () {
        // if (isCoinInWatchList(coinName)) {
        icon.classList.remove('fa-regular');
        icon.classList.add('fa-solid');

        icon.onclick = function () {
            removeFromWatchList(coinName, icon);
        };
        // } else {
        //     icon.classList.remove('fa-solid');
        //     icon.classList.add('fa-regular');
        // }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("fail", textStatus, errorThrown);
    })


    // window.location.href = '/';
}


function removeFromWatchList(coinName, icon) {
    var contextPath = "[[@{/}]]";
    var csrfHeaderName = "[[${_csrf.headerName}]]";
    var csrfValue = "[[${_csrf.token}]]";
    var url = "/" + "watchlist/" + coinName;

    icon.classList.remove('fa-solid');
    icon.classList.add('fa-regular');

    $.ajax({
        type: 'DELETE',
        url: url,
        // beforeSend: function (xhr){
        //     xhr.setRequestHeader(csrfHeaderName, csrfValue)
        // },
    }).done(function () {
        icon.classList.remove('fa-solid');
        icon.classList.add('fa-regular');

        icon.onclick = function () {
            addToWatchList(coinName, icon);
        };
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("fail", textStatus, errorThrown);
    })
}
