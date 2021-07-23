<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
     <title>引力透镜_个人相册</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1,user-scalable=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
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
    <link rel="stylesheet" href="<%=basePath %>html/css/h5reset.css"/>
    <title></title>
    <style>
        body{
            text-align: center;
        }
        img{
            max-width:100%;
            max-height:100%;
        }
        #top{
            background: url("<%=basePath %>html/img/top.png") no-repeat;
            background-size: 100% 100%;
            margin-bottom: 15px;
            height:230px;
        }
        @media (max-width: 320px){
            #top{height:168px;}
        }
        @media (max-width: 400px){
            #top{height:200px;}
        }
        .box{
            position: relative;
            border: 1px solid #ADC0EB;
            box-shadow: 0px 10px 10px #ADC0EB;
            border-radius: 15px;
            background-color: #EEF3FA;
            margin: 0px 20px 20px 20px;
            overflow: hidden;
        }
        .box>img{
            display: block;
            width:100%;
            height:100%;
        }
        .left_bottom{
            width: 30%;
            position: absolute;
            bottom: 230px;
            margin-left:-2px;
            left:0;
            display: none;
        }
        .header{
            margin-bottom:10px;
        }
        .right_bottom{
            width: 30%;
            position: absolute;
            bottom: 18%;
            margin-right:-8px;
            right:0;
            display: none;
        }
        #er{
            width: 50%;
            margin-top: 40px;
            margin-bottom: 50px;
        }
        .position{
			position: absolute;
			top: 0;
			right: 0;
		}
		.btn{
			width: 90%;
			margin: 0 auto;
			background-color: #0C2442;
			padding: 10px 0;
			font-size: 1.5rem;
			border-radius: 15px;
			color: #fff;
			font-weight: bold;
		}
		.little_box{
			position: absolute;
			top: 10px;
			right: 10px;
			width: 65px;
			height: 65px;
			border: 1px solid #000;
			border-radius: 25px;
			background-color: #ddd;
		}
    </style>
</head>
<body>
<div id="top">

</div>
<div class="header">
    <img src="<%=basePath %>html/img/header.png"/>
</div>
	<c:if test="${fn:length(photos) == 0}">
		<p>很抱歉，您的照片已被清理！</p>
		<p>我们只为您保留近1个月内的体验照片哦！</p>
	</c:if>
	<c:forEach items="${photos}" var="list" varStatus="vs">
	  <div class="box">
         <img src="${list}"/>
    </div>
	</c:forEach>
	
<div>
    <img src="<%=basePath %>html/img/er.png" id="er"/>
</div>
<img src="<%=basePath %>html/img/robot1.png" class="left_bottom"/>
<img src="<%=basePath %>html/img/robot3.png" class="left_bottom"/>
<img src="<%=basePath %>html/img/robot5.png" class="left_bottom"/>
<script>
    function random(){
        var random_n=parseInt(Math.random()*3);
        var robot=document.getElementsByClassName("left_bottom");
        robot[random_n].style.display="inline";
    }
    random();
</script>
<script type="text/javascript"
		src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script src="<%=basePath %>js/jquery-3.1.0.min.js"></script>
	<script src="<%=basePath %>js/httpService.js"></script>
<script type="text/javascript">
		$(function() {
			$.get('<%=basePath %>wechat/jssdk/sign.do', '', function(data) {
				if (data.code != 0) {
					return;
				}
				var d = data.data;
				wx.config({
					appId : d.appId,
					timestamp : d.timestamp,
					nonceStr : d.nonceStr,
					signature : d.signature,
					jsApiList : [ 'onMenuShareTimeline',
							'onMenuShareAppMessage', 'onMenuShareQQ',
							'onMenuShareQZone' ]
				});
				wx.ready(function() {
					//分享到朋友圈
					wx.onMenuShareTimeline({
						title : '${shareConfig.title}',
						link : '${shareConfig.link}' + '&share_type=1',
						desc : '${shareConfig.content}',
						imgUrl : '${shareConfig.imgUrl}',
						success : function() {
							//shareCallback('${shareConfig.id}', '1');
						},
						cancel : function() {
						}
					});
					//分享给朋友
					wx.onMenuShareAppMessage({
						title : '${shareConfig.title}',
						link : '${shareConfig.link}' + '&share_type=2',
						desc : '${shareConfig.content}',
						imgUrl : '${shareConfig.imgUrl}',
						success : function() {
							//shareCallback('${shareConfig.id}', '2');
						},
						cancel : function() {
						}
					});
					//分享到QQ
					wx.onMenuShareQQ({
						title : '${shareConfig.title}',
						link : '${shareConfig.link}' + '&share_type=3',
						desc : '${shareConfig.content}',
						imgUrl : '${shareConfig.imgUrl}',
						success : function() {
							//shareCallback('${shareConfig.id}', '3');
						},
						cancel : function() {
						}
					});
					//分享到QQ空间
					wx.onMenuShareQZone({
						title : '${shareConfig.title}',
						link : '${shareConfig.link}' + '&share_type=4',
						desc : '${shareConfig.content}',
						imgUrl : '${shareConfig.imgUrl}',
						success : function() {
							//shareCallback('${shareConfig.id}', '4');
						},
						cancel : function() {
						}
					});
				})
			}, 'json')
		});
	</script>
</body>
</html>