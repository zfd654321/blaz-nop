<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	height: 100%;
	background: url(/BLWXV3.0/html/wxcard/img/back1.png) no-repeat;
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
	height:280px;
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
	font-weight:900;
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
	width: 150px;
	height: 40px;
	font-size: 28px;
	line-height: 40px;
	text-align: center;
	border: solid 1px #333333
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
	src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/BLWXV3.0/javascript/jquery-3.1.0.min.js"></script>
<script src="/BLWXV3.0/javascript/httpService.js"></script>
</head>
<body>
<body>
	<div class="main">
		<div class="top">
			<img src="/BLWXV3.0/html/wxcard/img/banner1.png" width="750">
		</div>
		<c:if test="${empty error}">
		<div class="cardlist">
			<img id="card_tip" src="">
			<div class="cardtitle">1分代金券 </div>
			<div class="cardtext">卡卷说明卡卷说明卡卷说明卡卷说明卡卷说明卡卷说明卡卷说明</div>
			<div class="cardsub" id="addCard">领取卡卷</div>
		</div>
		</c:if>
	</div>
</body>
</body>
<c:if test="${empty error}">
<script type="text/javascript">
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
			wx.ready(function() {
				document.querySelector('#addCard').onclick = function() {
					var cardId = '${card_id}';
					var open_id = '${openid}';
					var key_id = '${key_id}';
					//因为知道 了card_id,再到后台调一次加密的动作
					$.get('/BLWXV3.0/wechat/jssdk/sign1.do', {
						"cardId" : cardId
					}, function(JsonData) {
						var data = JsonData.data
						data.cancel = function(res) {
						};

						data.success = function(res) {
							$('#addCard').remove();
							alert("成功");
							var len = res.length;
							if(len > 0) {
								var d = res[0];
								if(d.isSuccess) {
									var code = d.code;
									$.get('/BLWXV3.0/coupon/edit.do', {
										"cardId" : cardId,"key_id":key_id,"open_id":open_id,"code":code
									}, function(JsonData) {
									},"json");
								}
								
							}
							
						};
						
						data.complete = function(res) {
						};
						
						data.fail = function(res) {
						}
						wx.addCard(data);
						$('#addCard').remove();
					}, 'json')

				};

			})
		}, 'json')
	});
</script>
</c:if>
</html>