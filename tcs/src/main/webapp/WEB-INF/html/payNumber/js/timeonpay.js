function payByWX() {
    var number = $("#numberInput").val().toLowerCase();
    sendRequest(pageContextPath + '/number/timeOnWXPay.do', {
        "number": number
    }, function(jsonData) {
        var returndata = jsonData.data;
        if (returndata == "1") {
            $(".paydone").show();
        } else {
            alert("密码错误，请核对后重新输入");
        }
    });
}