var open_id = "";
var device_id = getURLParams("device_id");
var order_id = getURLParams("order_id");
var code = getURLParams('code');
var game_id = getURLParams("game_id");
var pay_able = false;
var paying = false;
var model_list="700303,600203,600201,600408,600407";

window.onload=function() {
	sendRequest(pageContextPath+'/WXPay/getWXUserOpenId.do', {
		"code" : code
	}, function(jsonData) {
		if (jsonData.data == '1') {
			open_id = jsonData.open_id;
			payable = true;
			savePayQrCodeScanInfo();
		} else {
			//alert('得到微信用户信息Error!');
		}
	});
	getCosplayList();
	t = setTimeout("checkPayTime()", 1000);
	pushHistory();
	document.addEventListener("visibilitychange", function() {
		if (document.visibilityState == 'hidden' && !paying) {
			cannelPay(order_id)
		}
	});

	window.addEventListener("popstate", function(e) {
		if (!paying) {
			cannelPay(order_id);
		}
	}, false);
};

function savePayQrCodeScanInfo(){
	sendRequest(pageContextPath+'/WXPay/savePayQrCodeScanInfo.do', {
		"order_id" : order_id,
		"device_id" : device_id,
		"open_id" : open_id,
		"pay_type" : "2",
	}, function(jsonData) {});
}

function saveUserAdvanceExperienceInfo(){
	sendRequest(pageContextPath+'/WXPay/saveUserAdvanceExperienceInfo.do', {
		"order_id" : order_id,
		"device_id" : device_id,
		"open_id" : open_id,
		"pay_type" : "2",
	}, function(jsonData) {});
}

function pushHistory() {
	var state = {
		title : "title",
		url : "#"
	};
	window.history.pushState(state, "title", "#");
};

var pay_time = 60;
var t;
function checkPayTime() {
	pay_time -= 1;
	$(".choosesum span").html(pay_time);
	$(".bottomline2 span").html(pay_time);
	if (pay_time == 0) {
		//$(".paysub").css("background", "#222");
		$(".paysub").html("支付超时");
		$(".choosesum").html("支付超时,请重新扫码");
		$(".bottomline2").html("支付超时,请重新扫码");
		$(".paysub").removeAttr("onclick");
		$(".cosplay").removeAttr("onclick");
		clearTimeout(t);
		pay_able = false;
	} else {
		if (pay_able) {
			t = setTimeout("checkPayTime()", 1000)
		}
	}
}
var payTimes = 0;
var payPrice = 0;
var payTimesA = 0;
var payPriceA = 0;
var payTimesB = 0;
var payPriceB = 0;

function getCosplayList() {
	sendRequest(pageContextPath+'/cosplay/getCosplayElement.do', {
		"device_id" : device_id,
		"order_id" : order_id,
		"open_id" : open_id,
		"game_id" : game_id
	}, function(jsonData) {
		if (jsonData.return_code == '1') {

			pay_time = jsonData.out_time;
			var priceList = jsonData.price.split(",");

			var priceListA = priceList[0].split("_");
			payTimesA = priceListA[0];
			payPriceA = priceListA[1];
			var priceListB = priceList[1].split("_");
			payTimesB = priceListB[0];
			payPriceB = priceListB[1];
			payTimes = Number(payTimesA);
			payPrice = payPriceA;
			$(".payPriceA").html(Number(payPriceA) / 100);
			$(".payPriceB").html(Number(payPriceB) / 100);
			$(".payTimesB").html(payTimesB);
			var str = "";
			$.each(jsonData.listCosplayElements, function(i, item) {
				str += '<div class="cosplay" id="' + item.cosplay_id
						+ '" onclick="openRepayOne(this)">';
				str += '<div class="cosplayimg">';
				str += '<div class="cosplaytip">' + checktip(item.cosplay_tip)
						+ '</div>';
				str += '<div class="icheck"></div>';
				str += '<div class="imgurl"><img src="'
						+ item.cosplay_image_url + '" /></div>';
				str += '</div><div class="cosplayinfo">';
				str += '<div class="cosplaytitle">' + item.cosplay_title
						+ '</div>';
				str += '<div class="cosplaytext">' + item.cosplay_desc
						+ '</div>';

				str += '<div class="cosplaydone" id="done' + item.cosplay_id
						+ '">' + item.order_count + '人体验</div>';
				// str += '<div class="cosplaypay"></div>';
				str += '</div></div>';
			});
			str += '<div class="clear"></div>'
			$(".list").html(str);
			pay_able = true;
		} else if (jsonData.return_code == '-1') {
			// alert("此订单已失效");
			wx.closeWindow();
		} else {
			alert("本机暂无相应的换装服务");
		}
	});
}

function checkPrice(price) {
	var str = "￥<span>" + (price / 100);
	str = str.replace(".", "</span>.");
	return str;
}

function checktip(tip) {
	var str = ""
	if (tip == "1") {
		str = "<img src='"+pageContextPath+"/html/payorder/img/cosplay_hot.png'>"
	} else if (tip == "2") {
		str = "<img src='"+pageContextPath+"/html/payorder/img/cosplay_new.png'>"
	}
	return str;
}
var cosplay_check_list = "";
var cosplay_check_count = 0;

function openRepayOne(_this) {
	openRepay(_this);
	$(".prepay").show();
	$(".prepay .preDiv").html($(_this).html());
	$(".mask").show();
	$(".mail_id").html($(_this).attr("id"))
}

function payMore() {
	saveUserAdvanceExperienceInfo();
	payTimes = Number(payTimesB);
	payPrice = payPriceB;
	hideMail();
	$(".bottomline2").hide();
	$(".bottomline").show();
	$(".teach").show();
	$(".cosplay").attr("onclick", "openRepay(this)");

}

function closeTeach() {
	//$(".paysub").css("background", "#222");
	$(".paysub").removeAttr("onclick");
	$(".teach").hide();
	$(".paysub").html(
			"请再选<span>" + (payTimes - cosplay_check_count) + "</span>件");
}

function openRepay(_this) {
	cosplay_id = $(_this).attr("id");
	if (hasThis(cosplay_id) && cosplay_check_count < payTimes) {
		cosplay_check_count += 1;
		$(".choosesum s1").html(cosplay_check_count);
		$(".redpoint").html(cosplay_check_count);
		$(".redpoint").show();
		if (cosplay_check_list != "") {
			cosplay_check_list += ","
		}
		cosplay_check_list += cosplay_id;
		var str = '';
		str += '<div class="mailinfo" id="mail' + cosplay_id + '">';
		str += '<div class="mailtittle">'
				+ $("#" + cosplay_id + " .cosplaytitle").html() + '</div>';
		str += '<div class="maildesc">'
				+ $("#" + cosplay_id + " .cosplaytext").html() + '</div>';
		str += '<div class="mailremove" onclick="removemail(\'' + cosplay_id
				+ '\')"></div>';
		str += '</div>';
		$(".mailline").append(str);

		str = '';
		str += '<div class="coschecked" id="checked' + cosplay_id + '">';
		$("#" + cosplay_id).append(str);

		$("#" + cosplay_id + " .icheck").show();
		if (cosplay_check_count == payTimes && pay_time > 0) {
			//$(".paysub").css("background", "#169bd5");
			$(".paysub").html("立即支付");
			$(".paysub").attr("onclick", "repay()");
		} else if (cosplay_check_count < payTimes && pay_time > 0) {
			$(".paysub")
					.html(
							"请再选<span>" + (payTimes - cosplay_check_count)
									+ "</span>件");
		}
	} else if (!hasThis(cosplay_id)) {
		removemail(cosplay_id);
	}
	// $(".mask").show();
	// $(".prepay").show();
	// $(".preImg img").attr("src",
	// $("#" + cosplay_id + " .imgurl img").attr("src"));
	// $(".preTittle").html($("#" + cosplay_id + " .cosplaytitle").html());
	// $(".prePrice").html($("#" + cosplay_id + " .cosplayprice").html());
	// $(".cosplaypay").attr("onclick", "repay('" + cosplay_id + "')")
}

function removemail(cosplay_id) {
	cosplay_check_count -= 1;
	$(".choosesum s1").html(cosplay_check_count);
	$("#mail" + cosplay_id).remove();
	$("#checked" + cosplay_id).remove();
	$("#" + cosplay_id + " .icheck").hide();
	var list = cosplay_check_list.split(",");
	var nlist = "";
	for (var i = 0; i < list.length; i++) {
		if (list[i] != cosplay_id) {
			if (nlist != "") {
				nlist += ",";
			}
			nlist += list[i];
		}
	}
	cosplay_check_list = nlist;
	//$(".paysub").css("background", "#222");
	$(".paysub").removeAttr("onclick");
	if (pay_time > 0) {
		$(".paysub").html(
				"请再选<span>" + (payTimes - cosplay_check_count) + "</span>件");
	}
	$(".redpoint").html(cosplay_check_count);
	if (cosplay_check_count == 0) {
		$(".redpoint").hide();
		hideMail();
	}
}

function hasThis(cosplay_id) {
	var list = cosplay_check_list.split(",");
	var hasit = true;
	for (var i = 0; i < list.length; i++) {
		if (list[i] == cosplay_id) {
			hasit = false;
		}
	}
	return hasit;
}

function closeMask() {
	$(".mask").hide();
	$(".prepay").hide();
	$(".modelPrePay").hide();
	if($(".mail_id").html()!=""){		
		removemail($(".mail_id").html());
	}
}

function repay() {
	saveUserAdvanceExperienceInfo();
	if (open_id != "" && pay_able) {
		pay_able = false;
		sendRequest(pageContextPath+'/cosplay/cosplayWXPay.do', {
			"device_id" : device_id,
			"order_id" : order_id,
			"open_id" : open_id,
			"cosplay_id" : cosplay_check_list,
			"less_pay_time" : pay_time
		}, function(jsonData) {
			var returndata = jsonData.data;
			var order_id = jsonData.order_id;
			var dataMap = jsonData.dataMap;
			if (returndata == "1") {
				paying = true;
				onBridgeReady(dataMap, order_id);
			} else {
				// alert("统一下单失败");
			}
		});
	}
}

function onBridgeReady(data, order_id) {
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId" : data.appId, // 公众号名称，由商户传入
		"timeStamp" : data.timeStamp, // 时间戳，自1970年以来的秒数
		"nonceStr" : data.nonceStr, // 随机串
		"package" : data.prepay_id,
		"signType" : data.signType, // 微信签名方式：
		"paySign" : data.paySign
	// 微信签名
	}, function(res) {
		
		pay_able = false;
		if (res.err_msg == "get_brand_wcpay_request:cancel"
				|| res.err_msg == "get_brand_wcpay_request:fail") {
			cannelPay(order_id);
		} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
		else {// 支付成功
			callBackWXPay(order_id);
			// alert("支付成功")
		}

	});
}

function callBackWXPay(order_id) {
	var url = pageContextPath+"/cosplay/callBackCosplayWXPay";
	var openId = open_id;
	sendRequest(url, {
		"order_id" : order_id,
		"openId" : openId,
		"cosplay_check_list" : cosplay_check_list
	}, function(jsonData) {
		paying = false;
		//if (jsonData.return_code == 1) {// 说明 支付成功
			var list = cosplay_check_list.split(",");

			clearTimeout(t);
			closeMask();
			$(".paydone").show();
			$(".list").hide();
			$(".bottomline").hide();
		//}
	});
	$("#bos").hide();
}

function cannelPay(order_id) {
	sendRequest(pageContextPath+"/cosplay/WXPayCancel", {
		"order_id" : order_id
	}, function(jsonData) {
		// alert("订单已被取消，请重新扫码下单。");
		//$(".paysub").css("background", "#222");
		$(".paysub").html("支付已取消");
		$(".choosesum").html("支付已取消,请重新扫码");
		$(".bottomline2").html("支付已取消,请重新扫码");
		$(".paysub").removeAttr("onclick");
		$(".cosplay").removeAttr("onclick");
		closeMask();
		clearTimeout(t);
		wx.closeWindow();
	});
}

function choosePrice(n) {
	if (n == "A") {
		payTimes = Number(payTimesA);
		payPrice = payPriceA;
	} else if (n == "B") {
		payTimes = Number(payTimesB);
		payPrice = payPriceB;
	} else {
		return;
	}
	$(".choosesum s2").html(payTimes);
	$(".choosepay").hide();
	$(".list").show();
	$(".bottomline").show();
	$(".paysub").html("请再选<span>" + payTimes + "</span>件");
}

function showMail() {
	if ($(".mailline").css("display") == "none" && cosplay_check_count > 0) {
		$(".mask").show();
		$(".mailline").slideDown();
	} else {
		hideMail();
	}
}

function hideMail() {
	$(".mask").hide();
	$(".mailline").slideUp();
	$(".prepay").hide();
}

var pageCheck = true;



function checkModel(){
	$(".mail_id").html("");
	$(".modelPrePay").show();
	$(".mask").show();
}

function modelpay(){
	payTimes = Number(payTimesB);
	cosplay_check_list=model_list;
	repay();
}

