var open_id = '';
var order_id = getQueryString("order_id");
var device_id = getQueryString("device_id");
var code = getQueryString('code');
var game_id = getQueryString("game_id");
var pay_able = false;
var paying = false;
var pay_time = 60;
function checkPayTime() {
	pay_time -= 1;
	if (pay_time == 0) {
		pay_able = false;
		clearTimeout(t);
		$(".bb").removeAttr("onclick");
		$(".bottime1").removeAttr("onclick");
		$(".timeless").html("订单已超时");
		if (pay_able) {
			// alert("该订单已超时，请重新扫码。");

		}
	} else {
		$(".timeless span").html(pay_time);
		t = setTimeout("checkPayTime()", 1000);
	}
}

var payTimesA = 0;
var payPriceA = 0;
var payTimesB = 0;
var payPriceB = 0;

window.onload = function() {
	test_coin = 1;
	setTimeout("goRoll()", 10);
	sendRequest(pageContextPath+'/WXPay/getWXUserOpenId.do', {
		"code" : code
	}, function(jsonData) {
		test_coin = 2;
		if (jsonData.data == '1') {
			open_id = jsonData.open_id;
			pay_able = true;
			savePayQrCodeScanInfo();
		} else {
			alert('得到微信用户信息Error!');
		}
	});

	sendRequest(pageContextPath+'/WXPay/getWXGameImg.do', {
		"game_id" : game_id,
		"device_id" : device_id,
		"order_id" : order_id
	}, function(jsonData) {
		test_coin = 3;
		$(".topimg img").attr("src", jsonData.imgvo.img1_url);
		$(".midimg img").attr("src", jsonData.imgvo.img2_url);
		if (jsonData.timeout == "1") {
			$(".timeless").html("订单已超时");
			// alert("该订单已超时，请重新扫码。");
			$(".bb").removeAttr("onclick");
			$(".bottime1").removeAttr("onclick");
		} else {
			pay_time = jsonData.out_time;
			checkPayTime();

			var priceList = jsonData.price.split(",");

			var priceListA = priceList[0].split("_");
			payTimesA = priceListA[0];
			payPriceA = priceListA[1];
			var priceListB = priceList[1].split("_");
			payTimesB = priceListB[0];
			payPriceB = priceListB[1];

			$(".time2").html(payPriceA / 100);
			$(".time1").html(payPriceB / 100);
		}

	});

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

function savePayQrCodeScanInfo() {
	sendRequest(pageContextPath+'/WXPay/savePayQrCodeScanInfo.do', {
		"order_id" : order_id,
		"device_id" : device_id,
		"open_id" : open_id,
		"pay_type" : "1",
	}, function(jsonData) {
	});
}

function saveUserAdvanceExperienceInfo() {
	sendRequest(pageContextPath+'/WXPay/saveUserAdvanceExperienceInfo.do', {
		"order_id" : order_id,
		"device_id" : device_id,
		"open_id" : open_id,
		"pay_type" : "1",
	}, function(jsonData) {
	});
}

function pushHistory() {
	var state = {
		title : "title",
		url : "#"
	};
	window.history.pushState(state, "title", "#");
};

var timeroll = 0;
var top_check = true;
function goRoll() {
	timeroll += 2
	if (timeroll >= 685) {
		timeroll = 0;
		top_check = !top_check
	}
	// if(top_check){
	// $(".bbtop").css("right",-timeroll-50+"px");
	// }else{
	$(".bbbottom").css("left", -timeroll - 50 + "px");
	// }
	setTimeout("goRoll()", 10);
}

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

function payByWX(pay_type) {
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
	if (open_id != "" && pay_able) {
		pay_able = false;
		$("#bos").show();
		sendRequest(pageContextPath+'/WXPay/payByWX.do', {
			"device_id" : device_id,
			"order_id" : order_id,
			"open_id" : open_id,
			"game_times" : payTime,
			"game_id" : game_id,
			"pay_money" : payPrice
		}, function(jsonData) {
			var returndata = jsonData.data;
			var order_id = jsonData.order_id;
			var dataMap = jsonData.dataMap;
			if (returndata == "1") {
				paying = true;
				onBridgeReady(dataMap, order_id, pay_type);
			} else if (returndata == "3") {
				if (pay_able) {
					// alert("该订单已超时，请重新扫码。");
					clearTimeout(t);
					$("#subline").hide();
				}
			} else {
				// alert("统一下单失败");
				$("#bos").hide();
			}
		});
	}

}
function onBridgeReady(data, order_id, pay_type) {
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId" : data.appId, // 公众号名称，由商户传入
		"timeStamp" : data.timeStamp, // 时间戳，自1970年以来的秒数
		"nonceStr" : data.nonceStr, // 随机串
		"package" : data.prepay_id,
		"signType" : data.signType, // 微信签名方式：
		"paySign" : data.paySign
	// 微信签名
	}, function(res) {
		paying = false;
		if (res.err_msg == "get_brand_wcpay_request:cancel"
				|| res.err_msg == "get_brand_wcpay_request:fail") {
			cannelPay(order_id);
		} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
		else {// 支付成功
			callBackWXPay(order_id);
		}
	});
}
// WX回凋 更改支付数据
function callBackWXPay(order_id, pay_type) {
	var url = pageContextPath+"/WXPay/callBackWXPay";
	var openId = open_id;
	sendRequest(url, {
		"order_id" : order_id,
		"device_id" : device_id
	}, function(jsonData) {
		// if (jsonData.data == 1) {// 说明 支付成功
		// alert("支付成功");
		pay_able = false;
		$(".paydone").show();

		// }
	});
	$("#bos").hide();
}

function cannelPay(order_id) {

	sendRequest(pageContextPath+"/cosplay/WXPayCancel", {
		"order_id" : order_id
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
