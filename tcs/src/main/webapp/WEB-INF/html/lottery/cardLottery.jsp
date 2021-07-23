<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="apple-itunes-app" content="app-id=429849944" />
<meta name="viewport" content="width=750,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
* {
	padding: 0;
	margin: 0;
}

html {
	width: 750px;
	margin: 0 auto
}

.main {
	position: fixed;
	overflow: scroll;
}

.top {
	width: 750px;
}

.text {
	color: #FFF;
	font-size: 40px;
}

.qrcode {
	width: 280px;
	margin: 0 auto;
}

.cardlist {
	position: relative;
	border: solid 1px #666666;
	height: 280px;
}

.cardlist img {
	width: 240px;
	height: 240px;
	padding: 20px;
}

.cardtitle {
	position: absolute;
	left: 300px;
	top: 20px;
	width: 400px;
	font-size: 35px;
	font-weight: 900;
}

.cardtext {
	position: absolute;
	left: 300px;
	top: 70px;
	width: 400px;
	font-size: 28px;
	text-align: justify;
}

.cardsub {
	position: absolute;
	left: 300px;
	bottom: 20px;
	width: 180px;
	height: 50px;
	font-size: 30px;
	line-height: 50px;
	text-align: center;
	border: solid 1px #333333;
	background: #f2571d;
	border-radius: 10px;
	color: #fff;
}

.done {
	background: #808080;
}

.cardlest {
	position: absolute;
	left: 500px;
	bottom: 20px;
	height: 40px;
	font-size: 20px;
	line-height: 40px;
	text-align: center;
}
</style>
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/BLWXV3.0/javascript/jquery-3.1.0.min.js"></script>
<script src="/BLWXV3.0/javascript/httpService.js"></script>
</head>
<body>
<body>
	<div class="main">
		<div class="top">
			<img src="/BLWXV3.0/html/lottery/img/banner.jpg" width="750">
		</div>
		<c:forEach items="${listAwardsCard}" var="entity" varStatus="vs">
			<div class="cardlist">
				<img id="card_tip" src="${entity.card_volume_image}">
				<div class="cardtitle">${entity.card_volume_name}</div>
				<div class="cardtext">${entity.card_volume_desc}</div>
				<div class="cardsub" onclick="addCard(this)"
					id="${entity.card_volume_id}">领取卡卷</div>
				<div class="cardlest"></div>
			</div>
		</c:forEach>

	</div>
</body>
</body>
<script type="text/javascript">
	var height = document.documentElement.clientHeight;
	var width = document.documentElement.clientWidth;
	$(".main").attr("style", "height:" + height + "px");
	$(function() {
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
		}, 'json')
		//var openId = '${openId}';
	});

	function addCard(_this) {
		var card_id = _this.id;
		$.get('/BLWXV3.0/wechat/jssdk/sign1.do', {
			"cardId" : card_id
		}, function(JsonData) {
			var data = JsonData.data
			data.cancel = function() {
			};

			data.success = function() {
				$(_this).addClass("done");
				$(_this).html("已领取");
				$(_this).removeAttr("onclick");
				saveCardInfo(card_id);
			};

			wx.addCard(data);
		}, 'json')
	}

	function saveCardInfo(card_id) {
		var device_id = getURLParams("device_id");
		var open_id = getURLParams("open_id");
		var awards_id = getURLParams("awards_id");
		var lottery_merchant_id = getURLParams("lottery_merchant_id");

		$.get('/BLWXV3.0/Lottery/saveGetCardInfo.do', 'open_id=' + open_id
				+ '&device_id=' + device_id + '&awards_id=' + awards_id
				+ '&CardId=' + card_id, function(data) {
			$("#spoil_image").attr("src", data.vo.spoil_image);
			$("#cardtitle").html(data.vo.spoil_name);
			$("#spoil_desc").html(data.vo.spoil_desc);
		}, 'json')
	}
</script>
</html>