var open_id = '';
var order_id = getQueryString("order_id");
var device_id = getQueryString("device_id");
var code = getQueryString('code');
var game_id = getQueryString("game_id");
var pay_able = false;
var paying = false;
var pay_time = 60;
var rst;

function checkPayTime() {
    pay_time -= 1;
    if (pay_time == 0) {
        pay_able = false;
        alert("该订单已超时，请重新扫码。");
        clearTimeout(rst);
        $(".paysub").removeAttr("onclick");
        // $(".paysub").html("超时");

    } else {
        // $(".paysub").html(pay_time);
        rst = setTimeout("checkPayTime()", 1000);
    }
}

var payTimesA = 0;
var payPriceA = 0;
var payTimesB = 0;
var payPriceB = 0;

window.onload = function() {
    test_coin = 1;
    loadOpenId();
    pushHistory();
    window.onpopstate = function() {
        if (!paying) {
            cannelPay(order_id);
        }
    };
    document.addEventListener("visibilitychange", function() {
        if (document.visibilityState == 'hidden' && !paying) {
            cannelPay(order_id)
        }
    });
};

function loadOpenId() {
    sendRequest(pageContextPath + '/WXPay/getWXUserOpenId.do', {
        "code": code
    }, function(jsonData) {
        var bottom = parseInt($(".main").css("bottom"))
        if (bottom < 0) {
            $(".main").css("top", bottom / 2);
        }
        test_coin = 2;
        if (jsonData.data == '1') {
            open_id = jsonData.open_id;

            savePayQrCodeScanInfo();
            loadTimeSet();
        } else {
            alert('得到微信用户信息Error!请重新扫码');
        }

    });
}

function loadTimeSet() {
    sendRequest(pageContextPath + '/timeon/getTimeOnSet.do', {
        "open_id": open_id,
        "device_id": device_id,
        "order_id": order_id
    }, function(jsonData) {
        test_coin = 3;
        if (jsonData.return_code == "-1") {
            $(".paysub").removeAttr("onclick");
            // $(".paysub").html("失效");
            alert('订单已失效!请重新扫码');
        } else if (jsonData.timeout == "1") {
            $(".paysub").removeAttr("onclick");
            // $(".paysub").html("超时");
            alert('订单已超时!请重新扫码');

        } else {
            pay_time = jsonData.out_time;
            checkPayTime();

            var priceList = jsonData.price.split(",");

            var priceListA = priceList[0].split("_");
            payTimesA = priceListA[0];
            payPriceA = priceListA[1];

            $(".paytime").html(Math.round(payTimesA / 60) + "");
            $(".pay").html(payPriceA / 100);
            pay_able = true;
            //$(".test").html("paybale为真");
            $(".paysub").attr("onClick", "payByWX(1)");
            $(".box").hide();
        }
    });
}

function savePayQrCodeScanInfo() {
    sendRequest(pageContextPath + '/WXPay/savePayQrCodeScanInfo.do', {
        "order_id": order_id,
        "device_id": device_id,
        "open_id": open_id,
        "pay_type": "1",
    }, function(jsonData) {});
}

function saveUserAdvanceExperienceInfo() {
    sendRequest(pageContextPath + '/WXPay/saveUserAdvanceExperienceInfo.do', {
        "order_id": order_id,
        "device_id": device_id,
        "open_id": open_id,
        "pay_type": "1",
    }, function(jsonData) {});
}

function pushHistory() {
    var state = {
        title: "title",
        url: "#"
    };
    window.history.pushState(state, "title", "#");
};

var timeroll = 0;
var top_check = true;


function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}

var dataMap;

function payByWX(pay_type) {
    if (paying) {
        onBridgeReady(dataMap, order_id, pay_type);
    } else if (open_id != "" && pay_able) {
        pay_able = false;
        paying = true;
        saveUserAdvanceExperienceInfo();
        var payTime = 0;
        var payPrice = 0;
        if (pay_type == 1) {
            payTime = payTimesA;
            payPrice = payPriceA;
        } else if (pay_type == 2) {
            payTime = payTimesB;
            payPrice = payPriceB;
        }
        //pay_able = false;
        //$(".test").html("paybale为假1");
        sendRequest(pageContextPath + '/timeon/timeOnWXPay.do', {
            "device_id": device_id,
            "order_id": order_id,
            "open_id": open_id,
            "on_time": payTime,
            "game_id": game_id,
            "pay_money": payPrice
        }, function(jsonData) {
            var returndata = jsonData.data;
            //$(".test").html("paybale为假11;returndata:"+returndata);
            order_id = jsonData.order_id;
            dataMap = jsonData.dataMap;
            if (returndata == "1") {

                pay_time = 60;
                onBridgeReady(dataMap, order_id, pay_type);
            } else if (returndata == "3") {
                alert('订单已超时!请重新扫码');
                $(".paysub").removeAttr("onclick");
                // $(".paysub").html("超时");
            } else {
                //alert("订单无效!请重新扫码");
                //$("#bos").hide();
            }
        });
    } else {
        if (pay_time < 50) {
            alert("订单无效!请重新扫码")
        }

    }



}

var data_l = null;

function onBridgeReady(data, order_id, pay_type) {
    //$(".test").html("paybale为假12");
    WeixinJSBridge.invoke('getBrandWCPayRequest', {
        "appId": data.appId, // 公众号名称，由商户传入
        "timeStamp": data.timeStamp, // 时间戳，自1970年以来的秒数
        "nonceStr": data.nonceStr, // 随机串
        "package": data.prepay_id,
        "signType": data.signType, // 微信签名方式：
        "paySign": data.paySign
            // 微信签名
    }, function(res) {
        //$(".test").html("paybale为假13;res:"+res);
        paying = false;
        //pay_able = false;
        if (res.err_msg == "get_brand_wcpay_request:cancel" ||
            res.err_msg == "get_brand_wcpay_request:fail") {
            cannelPay(order_id);
        } // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
        else { // 支付成功
            callBackWXPay(order_id);
        }
    });
}
// WX回凋 更改支付数据
function callBackWXPay(order_id, pay_type) {
    var url = pageContextPath + "/timeon/callBackTimeOnWXPay";
    var openId = open_id;
    sendRequest(url, {
        "order_id": order_id,
        "device_id": device_id
    }, function(jsonData) {
        // if (jsonData.data == 1) {// 说明 支付成功
        // alert("支付成功");
        pay_able = false;
        //$(".test").html("paybale为假2");
        $(".paydone").show();
        clearTimeout(rst);
        // }
    });
    //$("#bos").hide();
}

function cannelPay(order_id) {
    sendRequest(pageContextPath + "/timeon/WXPayCancel", {
        "order_id": order_id
    }, function(jsonData) {
        if (pay_able) {
            // alert("订单已被取消，请重新扫码下单。");
        }
        wx.closeWindow();
    });

}

var test_coin = 0;

function pay_test() {
    alert("test_begin");
    alert("open_id:" + open_id);
    alert("test_coin:" + test_coin);
}