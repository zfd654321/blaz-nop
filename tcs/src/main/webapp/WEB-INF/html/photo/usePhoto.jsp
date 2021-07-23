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
                    <link rel="stylesheet" href="<%=basePath %>html/css/h5reset.css" />
                    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
                    <script src="<%=basePath %>js/httpService.js"></script>
                    <script>
                        var img_src = "";
                        var openId = '${openId}';
                        var device_id = '${device_id}';

                        function img_click(_this) {
                            if (_this.className == "little_box") {
                                _this.className = "little_box linshi";
                                var elem = document.createElement("img");
                                elem.className = "position";
                                elem.src = "<%=basePath %>html/img/ok.png";
                                _this.appendChild(elem)
                            } else {
                                _this.className = "little_box";
                                _this.removeChild(_this.getElementsByClassName("position")[0]);
                            }
                        }

                        function btn_sumbit() {
                            var position = document.getElementsByClassName("position");
                            for (var i = 0; i < position.length; i++) {
                                img_src += position[i].parentNode.nextElementSibling.src + ",";
                            }
                            img_src = img_src.substring(0, img_src.length - 1);
                        }

                        function doprint() {
                            var img_url_list = "";
                            var img_code_list = "";
                            var position = document.getElementsByClassName("position");
                            for (var i = 0; i < position.length; i++) {
                                if (img_url_list != "") {
                                    img_url_list += ",";
                                }
                                img_url_list += position[i].parentNode.nextElementSibling.src;
                                if (img_code_list != "") {
                                    img_code_list += ",";
                                }
                                img_code_list += position[i].parentNode.nextElementSibling.name;
                            }
                            if (img_code_list == "") {
                                alert("请勾选要打印的照片");
                                return;
                            }
                            //alert(img_code_list);
                            var openId = '${openId}';
                            var device_id = '${device_id}';
                            $("#bos").show();
                            sendRequest('<%=basePath %>WXPrinter/payPrinter.do', {
                                'openId': openId,
                                'img_code_list': img_code_list,
                                'device_id': device_id
                            }, function(jsonData) {
                                var return_code = jsonData.return_code;
                                var printer_order = jsonData.printer_order;
                                var dataMap = jsonData.dataMap;
                                var openId = jsonData.openId;
                                if (return_code == '1') {
                                    onBridgeReady(dataMap, printer_order, openId);
                                } else if (return_code == '-2') {
                                    alert(jsonData.return_msg);
                                    $("#bos").hide();
                                } else if (return_code == '-3') {
                                    alert(jsonData.return_msg);
                                    $("#bos").hide();
                                } else if (return_code == '-1') {
                                    alert(jsonData.return_msg);
                                    $("#bos").hide();
                                } else {
                                    alert("微信统一下订单失败,请稍后尝试");
                                    $("#bos").hide();
                                }
                            });
                        }

                        function onBridgeReady(data, printer_order, openId) {
                            WeixinJSBridge.invoke('getBrandWCPayRequest', {
                                "appId": data.appId, //公众号名称，由商户传入     
                                "timeStamp": data.timeStamp, //时间戳，自1970年以来的秒数     
                                "nonceStr": data.nonceStr, //随机串     
                                "package": data.prepay_id,
                                "signType": data.signType, //微信签名方式：     
                                "paySign": data.paySign
                                    //微信签名 
                            }, function(res) {
                                if (res.err_msg == "get_brand_wcpay_request:cancel" ||
                                    res.err_msg == "get_brand_wcpay_request:fail") {
                                    alert("调微信支付失败!");
                                    $("#bos").hide();
                                } // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                                else { //支付成功
                                    callBackWXPay(printer_order, openId);
                                    //alert("支付成功")
                                }
                            });
                        }

                        //WX回凋 更改支付数据
                        function callBackWXPay(printer_order, openId) {
                            var url = "<%=basePath %>WXPrinter/callBackWXPay.do";
                            sendRequest(url, {
                                "printer_order": printer_order,
                                "openId": openId,
                            }, function(jsonData) {
                                if (jsonData.data == 1) { //说明 支付成功
                                    alert("支付成功");
                                    /*  // window.location.href='<%=basePath %>wxshop/getUserOrder.do?openId=${openId}&order_status=3';*/
                                    $("#subline").hide();
                                }
                            });
                            $("#bos").hide();
                        }
                    </script>
                    <title></title>
                    <style>
                        body {
                            text-align: center;
                        }
                        
                        img {
                            max-width: 100%;
                            max-height: 100%;
                        }
                        
                        #top {
                            background: url("<%=basePath %>html/img/top.png") no-repeat;
                            background-size: 100% 100%;
                            margin-bottom: 15px;
                            height: 230px;
                        }
                        
                        @media ( max-width: 320px) {
                            #top {
                                height: 168px;
                            }
                        }
                        
                        @media ( max-width: 400px) {
                            #top {
                                height: 200px;
                            }
                        }
                        
                        .box {
                            position: relative;
                            border: 1px solid #ADC0EB;
                            box-shadow: 0px 10px 10px #ADC0EB;
                            border-radius: 15px;
                            background-color: #EEF3FA;
                            margin: 0px 20px 20px 20px;
                            overflow: hidden;
                        }
                        
                        .box>img {
                            display: block;
                            width: 100%;
                            height: 100%;
                        }
                        
                        .left_bottom {
                            width: 30%;
                            position: absolute;
                            bottom: 230px;
                            margin-left: -2px;
                            left: 0;
                            display: none;
                        }
                        
                        .header {
                            margin-bottom: 10px;
                        }
                        
                        .right_bottom {
                            width: 30%;
                            position: absolute;
                            bottom: 18%;
                            margin-right: -8px;
                            right: 0;
                            display: none;
                        }
                        
                        #er {
                            width: 50%;
                            margin-top: 40px;
                            margin-bottom: 50px;
                        }
                        
                        .position {
                            position: absolute;
                            top: 0;
                            right: 0;
                        }
                        
                        .btn {
                            width: 90%;
                            margin: 0 auto;
                            background-color: #0C2442;
                            padding: 10px 0;
                            font-size: 1.5rem;
                            border-radius: 15px;
                            color: #fff;
                            font-weight: bold;
                        }
                        
                        .little_box {
                            position: absolute;
                            top: 10px;
                            right: 10px;
                            width: 26px;
                            height: 26px;
                            border: 1px solid #000;
                            border-radius: 10px;
                            background-color: #ddd;
                        }
                        
                        .p_fiexd {
                            position: fixed;
                            padding-bottom: 5px;
                            bottom: 0;
                            left: 0;
                            right: 0;
                            font-weight: bold;
                            font-size: .9rem;
                            background-color: #ddd;
                        }
                        
                        .print {
                            width: 20px;
                            font-size: 20px;
                            position: fixed;
                            right: 0;
                            bottom: 50px;
                            background: #ffaaff;
                            padding: 10px;
                            border-radius: 10px 0 0 10px;
                            font-weight: 900;
                            display: none;
                        }
                        
                        .facebook {
                            width: 20px;
                            height: 28px;
                            font-size: 20px;
                            position: fixed;
                            right: 0;
                            bottom: 130px;
                            background: url(<%=basePath %>html/img/facebook.jpg);
                            background-size: 100%;
                            padding: 10px;
                            border-radius: 10px 0 0 10px;
                            font-weight: 900;
                            display: none;
                        }
                        
                        .bos {
                            position: fixed;
                            background: #000;
                            opacity: 0.7;
                            width: 100%;
                            height: 100%;
                            z-index: 10;
                            display: none;
                            z-index: 15
                        }
                        
                        .bos img {
                            margin: 200px auto;
                        }
                        
                        p {
                            font-size: 20px;
                        }
                    </style>
                </head>

                <body>
                    <div class="bos" id="bos">
                        <img src="<%=basePath %>html/img/5-121204193R5-50.gif" width="40" height="40" />
                    </div>
                    <div id="top"></div>
                    <div class="header">
                        <img src="<%=basePath %>html/img/header.png" />
                    </div>
                    <c:if test="${fn:length(photos) == 0}">
                        <p>很抱歉，您的照片已被清理！</p>
                        <p>我们只为您保留近1个月内的体验照片哦！</p>
                    </c:if>
                    <c:forEach items="${photos}" var="list" varStatus="vs">
                        <div class="box">
                            <div class="little_box" onclick="img_click(this)">

                            </div>
                            <img src="${list.img_url}" name="${list.photo_code}" />
                        </div>
                    </c:forEach>
                    <div class="p_fiexd">勾选照片右上角点击分享能得到所需要的照片</div>
                    <div style="margin-top: 100px;">
                        <img src="<%=basePath %>html/img/er.png" id="er" />
                    </div>
                    <img src="<%=basePath %>html/img/robot1.png" class="left_bottom" />
                    <img src="<%=basePath %>html/img/robot3.png" class="left_bottom" />
                    <img src="<%=basePath %>html/img/robot5.png" class="left_bottom" />
                    <div class="print" onclick="doprint()">打印</div>
                    <div class="facebook" onclick="faceShare()"></div>
                    <script>
                        function random() {
                            var random_n = parseInt(Math.random() * 3);
                            var robot = document.getElementsByClassName("left_bottom");
                            robot[random_n].style.display = "inline";
                        }
                        random();

                        function faceShare() {
                            shareCallback('${shareConfig.id}', '1');
                            var url = 'http://www.facebook.com/sharer.php?u=' +
                                encodeURIComponent('http://bltest.shishiar.cn<%=basePath %>wechat/share/getuserPhoto.do?fromopenId=${openId}&content=${shareConfig.id}') +
                                '&t=' +
                                encodeURIComponent(document.title);
                            location.href = url;

                        }

                        function shareCallback(id, share_type) {
                            var position = document.getElementsByClassName("position");
                            for (var i = 0; i < position.length; i++) {
                                img_src += position[i].parentNode.nextElementSibling.src +
                                    ",";
                            }

                            img_src = img_src.substring(0, img_src.length - 1);
                            $.get('<%=basePath %>wechat/share/callback.do', {
                                'openId': openId,
                                'id': id,
                                'img_src': img_src,
                                'share_type': share_type,
                                'device_id': device_id
                            }, function(data) {
                                if (data.code == 0) {
                                    return;
                                }
                            }, 'json');
                        }
                    </script>
                    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
                    <script src="<%=basePath %>js/jquery-3.1.0.min.js"></script>
                    <script src="<%=basePath %>js/httpService.js"></script>
                    <script type="text/javascript">
                        $(function() {
                            var printer = '${printer}';
                            if (printer == "1") {
                                $(".print").show();
                            }
                            var faceshare = '${faceshare}';
                            if (faceshare == "1") {
                                $(".facebook").show();
                            }
                            $.get('<%=basePath %>wechat/jssdk/sign.do', '', function(data) {
                                if (data.code != 0) {
                                    return;
                                }
                                var d = data.data;
                                wx.config({
                                    appId: d.appId,
                                    timestamp: d.timestamp,
                                    nonceStr: d.nonceStr,
                                    signature: d.signature,
                                    jsApiList: ['onMenuShareTimeline',
                                        'onMenuShareAppMessage', 'onMenuShareQQ',
                                        'onMenuShareQZone'
                                    ]
                                });
                                wx.ready(function() {
                                    //分享到朋友圈
                                    wx.onMenuShareTimeline({
                                        title: '${shareConfig.title}',
                                        link: '${shareConfig.link}' + '&share_type=1',
                                        desc: '${shareConfig.content}',
                                        imgUrl: '${shareConfig.imgUrl}',
                                        success: function() {
                                            shareCallback('${shareConfig.id}', '1');
                                        },
                                        cancel: function() {}
                                    });
                                    //分享给朋友
                                    wx.onMenuShareAppMessage({
                                        title: '${shareConfig.title}',
                                        link: '${shareConfig.link}' + '&share_type=2',
                                        desc: '${shareConfig.content}',
                                        imgUrl: '${shareConfig.imgUrl}',
                                        success: function() {
                                            shareCallback('${shareConfig.id}', '2');
                                        },
                                        cancel: function() {}
                                    });
                                    //分享到QQ
                                    wx.onMenuShareQQ({
                                        title: '${shareConfig.title}',
                                        link: '${shareConfig.link}' + '&share_type=3',
                                        desc: '${shareConfig.content}',
                                        imgUrl: '${shareConfig.imgUrl}',
                                        success: function() {
                                            shareCallback('${shareConfig.id}', '3');
                                        },
                                        cancel: function() {}
                                    });
                                    //分享到QQ空间
                                    wx.onMenuShareQZone({
                                        title: '${shareConfig.title}',
                                        link: '${shareConfig.link}' + '&share_type=4',
                                        desc: '${shareConfig.content}',
                                        imgUrl: '${shareConfig.imgUrl}',
                                        success: function() {
                                            shareCallback('${shareConfig.id}', '4');
                                        },
                                        cancel: function() {}
                                    });
                                })
                            }, 'json')



                            pushHistory();
                        });

                        function pushHistory() {
                            var state = {
                                title: "title",
                                url: "#"
                            };
                            window.history.pushState(state, "title", "#");
                        };
                    </script>
                </body>

                </html>