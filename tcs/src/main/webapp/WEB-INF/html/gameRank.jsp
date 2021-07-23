<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1,user-scalable=no">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!-- 启用360浏览器的极速模式(webkit) -->
<meta name="renderer" content="webkit">
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
<meta name="HandheldFriendly" content="true">
<!-- 微软的老式浏览器 -->
<meta name="MobileOptimized" content="320">
<!-- uc强制竖屏 -->
<meta name="screen-orientation" content="portrait">
<!-- QQ强制竖屏 -->
<meta name="x5-orientation" content="portrait">
<!-- UC强制全屏 -->
<meta name="full-screen" content="yes">
<!-- QQ强制全屏 -->
<meta name="x5-fullscreen" content="true">
<!-- UC应用模式 -->
<meta name="browsermode" content="application">
<!-- QQ应用模式 -->
<meta name="x5-page-mode" content="app">
<!-- windows phone 点击无高光 -->
<meta name="msapplication-tap-highlight" content="no">
<!-- 适应移动端end -->
<link rel="stylesheet" href="<%=basePath %>css/h5reset.css" />
<title>博乐信息游戏排行榜</title>
<style>
body {
	text-align: center;
	font-size: 18px;
	font-family: "Microsoft YaHei";
	font-weight: bold;
	color: #284483;
}

img {
	max-width: 100%;
}

.header>img {
	display: block;
}

.line {
	background-color: rgba(23, 74, 153, .4);
	height: 5px;
}

.content:after {
	content: "";
	display: block;
	visibility: hidden;
	clear: both;
}

.content>div {
	float: left;
	height: 60px;
	line-height: 60px;
}

.content>div.number {
	width: 18%;
	text-align: right;
	box-sizing: border-box;
	padding-right: 10px;
}

.content>div.image {
	width: 12%;
}

.content>div.image>span {
	display: inline-block;
	margin-top: 20%;
	width: 100%;
	height: 65%;
	border-radius: 50%;
	background-color: #76A2C7;
}

.content>div.image>span>span {
	display: inline-block;
	width: 100%;
	height: 100%;
	border-radius: 50%;
	background-color: #fff;
	position: relative;
	top: -3px;
}

.content>div.image>span>span>.head {
	border-radius: 50%;
}

.content>div.image>span>span>.no1 {
	position: absolute;
	top: -60%;
	left: 10%;
	width: 80%;
	height: 80%;
}

.content>div.image>span>span>img {
	width: 100%;
	height: 100%;
}

.content>div.name {
	width: 35%;
	overflow: hidden;
}

.content>div.score {
	width: 35%;
}

.erweima {
	margin: 50px 0 90px 0;
}

.erweima>img {
	width: 60%;
}

footer {
	position: fixed;
	bottom: 0;
	height: 10%;
	background-color: #4B6FB9;
	color: #ffffff;
	width: 100%;
}

footer>.my_img {
	float: left;
	width: 40%;
}

footer>.my_img>.img_main {
	width: 80%;
}

footer>.my_name {
	float: left;
	width: 30%;
	line-height: 100%;
}

footer>.my_score {
	float: left;
	width: 30%;
}
</style>
</head>
<body>

	<div class="header">
		<img src="${GameRanklistDto.game_html_image}" />
	</div>
	<div class="line"></div>
	<c:forEach items="${GameRanklistDto.listrank}" var="list"
		varStatus="vs">
		<div class="content">
			<div class="number">${list.order_no}</div>
			<div class="image">
				<span> 
				   <span> 
				       <img src="${list.blGameRankingListVo.headimgurl}" class="head" />
				       <c:if test="${list.order_no==1}">
					   <img src="<%=basePath %>img/no1.png" class="no1" />
					   </c:if>
					    <c:if test="${list.order_no==2}">
					   <img src="<%=basePath %>img/no2.png" class="no1" />
					   </c:if>
					    <c:if test="${list.order_no==3}">
					   <img src="<%=basePath %>img/no3.png" class="no1" />
					   </c:if>
				 </span>
				</span>
			</div>
			<div class="name">${list.blGameRankingListVo.nickname}</div>
			<div class="score">${list.blGameRankingListVo.score}</div>
		</div>
		
		
	</c:forEach>


	<div class="erweima">
		<img src="<%=basePath %>img/er.png" />
	</div>
	<footer>
		<div class="my_img">
			<img src="<%=basePath %>img/my.png" class="img_main" />
		</div>
		<div class="my_name">${userRank}</div>
		<div class="my_score">${userScore}</div>
	</footer>
	<script>
		window.onload = function() {
			function ready() {
				var content = document.getElementsByClassName("content");
				for (var i = 1; i < content.length; i++) {
					if (i % 2 != 0) {
						content[i].style.backgroundColor = "#B9D8EA";
					}
				}
				var main_height = parseFloat(window.getComputedStyle(document
						.getElementsByTagName("footer")[0]).height) / 2;
				var img_height = parseFloat(window.getComputedStyle(document
						.getElementsByClassName("img_main")[0]).height) / 2;
				var myName_height = parseFloat(window.getComputedStyle(document
						.getElementsByClassName("my_name")[0]).height) / 2;
				var myScore_height = parseFloat(window.getComputedStyle(document
						.getElementsByClassName("my_score")[0]).height) / 2;
				document.getElementsByClassName("img_main")[0].style.marginTop = main_height
						- img_height + "px";
				document.getElementsByClassName("my_name")[0].style.marginTop = main_height
						- myName_height + "px";
				document.getElementsByClassName("my_score")[0].style.marginTop = main_height
						- myScore_height + "px";
			}
			ready();
		}
	</script>
</body>
</html>