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
	width: 150px;
	height: 40px;
	font-size: 28px;
	line-height: 40px;
	text-align: center;
	border: solid 1px #333333;
	background: #098809;
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
.nosptext{
	width: 750px;
	text-align: center;
	font-size: 35px;
	margin: 30px 0;
}
</style>
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/BLWXV3.0/javascript/jquery-3.1.0.min.js"></script>
<script src="/BLWXV3.0/javascript/httpService.js"></script>
</head>
<body>
	<div class="main">
		<div class="top">
			<img src="/BLWXV3.0/html/lottery/img/banner.jpg" width="750">
		</div>
		<div class="nosptext">您已经领取过奖品了</div>

	</div>
</body>
<script type="text/javascript">
	var height = document.documentElement.clientHeight;
	var width = document.documentElement.clientWidth;
	$(".main").attr("style", "min-height:" + height + "px");
</script>
</html>