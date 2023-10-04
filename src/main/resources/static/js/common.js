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
    }).done(function (){
        console.log("done")
    }).fail(function (jqXHR, textStatus, errorThrown){
        console.log("fail", textStatus, errorThrown);
    })



    window.location.href = '/';
}


function getCurrency(){
    var currency = localStorage.getItem('selectedCurrency', JSON.stringify(data));
    if(currency == null) return "/"

    return currency;
}


