var open_id = getURLParams("open_id");
var AwardsList;
var award;
var spoil;
var order_id = getURLParams("order_id");
var device_id = getURLParams('device_id');
$(function() {
	rollTimes = $("#lotteryCount").val();
	$("#rollTimes").html(rollTimes);
	var height = window.screen.height;
	var width = window.screen.width;
	height = 750 / width * height;
	$(".main").attr("style", "height:" + height + "px");
	document.ontouchmove = function(e) {
		e.preventDefault();
	}

	$.get('/BLWXV3.0/wechat/jssdk/sign.do', '', function(data) {
		if (data.code != 0) {
			return;
		}
		var d = data.data;
		wx.config({
			appId : d.appId,
			timestamp : d.timestamp,
			nonceStr : d.nonceStr,
			signature : d.signature,
			jsApiList : [ 'addCard' ]
		});
	}, 'json');
	checkRoll();
});
function checkRoll() {
	sendRequest('/BLWXV3.0/PostPayDraw/getDialDrawAwardsList.do', {
		"order_id" : order_id,
		"device_id" : device_id,
		"open_id" : open_id
	}, function(jsonData) {
		if (jsonData.return_code == '1') {
			AwardsList = jsonData.listDrawAwards;
			for (var i = 0; i < AwardsList.length; i++) {
				$("#award" + i).html(AwardsList[i].awards_name);
			}
			$("#i_cont").attr("src", "/BLWXV3.0/html/redpacket/img/roll.png");
			rotate.flag_click = true;
		} else {
			$("#i_cont").attr("src",
					"/BLWXV3.0/html/redpacket/img/rollunable.png");
			alert(jsonData.return_msg);
		}
	});

}

function addCard(card_id) {
	$.get('/BLWXV3.0/wechat/jssdk/sign1.do', {
		"cardId" : card_id
	}, function(JsonData) {
		var data = JsonData.data
		data.cancel = function() {
		};

		data.success = function() {
			saveCardInfo();
		};

		wx.addCard(data);
	}, 'json')
}

function saveCardInfo(card_id) {
	var device_id = getURLParams("device_id");
	var open_id = getURLParams("open_id");
	var awards_id = getURLParams("awards_id");
	var lottery_merchant_id = getURLParams("lottery_merchant_id");

	$.get('/BLWXV3.0/PostPayDraw/sendCouponInfo.do', {
		"open_id" : open_id,
		"device_id" : device_id,
		"order_id" : order_id,
		"awards_id" : award.awards_id,
		"spoil_id" : spoil.spoil_id
	}, function(data) {

	}, 'json')
}

var result_angle = [ {
	a : 0,
}, {
	a : 60,
}, {
	a : 120,
}, {
	a : 180,
}, {
	a : 240,
}, {
	a : 300,
} ];
var during_time = 3;// during_time:旋转持续时间(s)

var rollTimes = 0;

var user_lottery_id = ""
var rotate = {
	rotate_angle : 0, // 起始位置为0
	flag_click : false, // 转盘转动过程中不可再次触发
	calculate_result : function() {
		roll();
	}
}
$(document).ready(function() {

	$('#i_cont').click(function() {
		if (rotate.flag_click) { // 旋转结束前，不允许再次触发
			roll();
		}
	});
});
function roll() {
	var self = rotate;
	during_time = during_time || 3; // 默认为3s
	var result_index; // 最终要旋转到哪一块，对应result_angle的下标
	var start_pos = end_pos = 0; // 判断的角度值起始位置和结束位置

	sendRequest('/BLWXV3.0/PostPayDraw/getDrawInfo.do', {
		"open_id" : open_id,
		"device_id" : device_id,
		"order_id" : order_id
	}, function(jsonData) {
		award = jsonData.DrawAwards;
		spoil = jsonData.DrawSpoild;
		for (var i = 0; i < AwardsList.length; i++) {
			if (AwardsList[i].awards_id == award.awards_id) {
				result_index = i;
			}
		}
		if (award.draw_type == "1") {
			$(".s1sub").html("领取奖品");
		} else if (award.draw_type == "2") {
			$(".s1sub").html("领取卡卷");
		} else if (award.draw_type == "3") {
			$(".s1sub").html("领取红包");
		}
		// result_index = jsonData.prize_id;// **--从服务器获取轮盘停留位置序号--**
		user_lottery_id = jsonData.user_lottery_id;// **--保留当前的奖品编号--**
		lotteryCount = jsonData.lotteryCount;// 获得剩余次数
		if (lotteryCount != null) {
			rollTimes = lotteryCount;
		} else {
			rollTimes = rollTimes - 1;
		}
		checkRollTime();
		var rand_circle = Math.ceil(Math.random() * 2) + 2; // 附加多转几圈，2-4
		self.flag_click = false; // 旋转结束前，不允许再次触发
		self.rotate_angle = self.rotate_angle - rand_circle * 360
				- result_angle[result_index].a - self.rotate_angle % 360;
		$('#i_bg').css(
				{
					'transform' : 'rotate(' + self.rotate_angle + 'deg)',
					'-ms-transform' : 'rotate(' + self.rotate_angle + 'deg)',
					'-webkit-transform' : 'rotate(' + self.rotate_angle
							+ 'deg)',
					'-moz-transform' : 'rotate(' + self.rotate_angle + 'deg)',
					'-o-transform' : 'rotate(' + self.rotate_angle + 'deg)',
					'transition' : 'transform ease-out ' + during_time + 's',
					'-moz-transition' : '-moz-transform ease-out '
							+ during_time + 's',
					'-webkit-transition' : '-webkit-transform ease-out '
							+ during_time + 's',
					'-o-transition' : '-o-transform ease-out ' + during_time
							+ 's'
				});

		// 旋转结束后，允许再次触发
		setTimeout(function() {
			self.flag_click = true;
			$(".mask").show();
			$(".s1").show();
		}, during_time * 1000);
	});
}
function doredpack() {
	if (award.draw_type == "1") {
		sendRequest('/BLWXV3.0/PostPayDraw/getGoodsImage.do', {
			"open_id" : open_id,
			"device_id" : device_id,
			"order_id" : order_id,
			"awards_id" : award.awards_id,
			"spoil_id" : spoil.spoil_id
		}, function(jsonData) {
			if (jsonData.return_code == "1") {
				$(".s1").hide();
				$(".s2 #spoild_name").html(spoil.spoil_name);
				$(".s2 img").attr("src", jsonData.qr_code);
				$(".s2").show();
			} else {
				alert(jsonData.return_msg);
			}
		});
	} else if (award.draw_type == "2") {
		addCard(spoil.spoil_id);
	} else if (award.draw_type == "3") {
		sendRequest('/BLWXV3.0/PostPayDraw/sendCashRedPacket.do', {
			"open_id" : open_id,
			"device_id" : device_id,
			"order_id" : order_id,
			"awards_id" : award.awards_id,
			"spoil_id" : spoil.spoil_id
		}, function(jsonData) {
			if (jsonData.return_code == "1") {
				alert("请关注"+"搏乐互动娱乐"+"公众号领取成功");
				$(".mask").hide();
				$(".s1").hide();
			} else {
				alert("领取失败，请联系工作人员");
			}
		});
	}
}

function goinfo() {
	location.href = "gotoHughTestSite"
}

function checkRollTime() {
	if (rollTimes > 0) {
		$("#i_cont").attr("src", "/BLWXV3.0/html/redpacket/img/roll.png");
	} else {
		$("#i_cont").attr("src", "/BLWXV3.0/html/redpacket/img/rollunable.png");
		rotate.flag_click = false;
	}
}