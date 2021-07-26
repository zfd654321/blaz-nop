var pageContextPath = '/oms';
/**

* 使用post方式向后台发送请求
 * @param url 请求的URL
 * @param data 请求的参数
 * @param succssFun  加载成功后的函数
sendRequest(pageContextPath+"",{"":}, function (jsonData){
	if(jsonData.flag=="ok"){
		
	}
}, true);
 */
function sendRequest(url, data, succssFun, asy) {
    if (asy == undefined || asy == true) {
        asy = true;
    }
    $.ajax({
        url: url,
        dataType: 'json',
        type: 'post',
        data: data,
        async: asy,
        success: function(jsonData) {
            //将后台响应的数据转换成json数据
            if (jsonData.code == "login") {
                top.location.href = pageContextPath + "/login.html";
            } else {
                succssFun(jsonData);
            }
        },
        error: function(jsonData) {
            alert("对不起，系统正忙请您稍后重试!");
        }
    });
}
/**
 * 使用get方式向后台发送请求
 * @param url 请求的URL
 * @param data 请求的参数
 * @param succssFun  加载成功后的函数
 */
function sendRequestByGet(url, data, succssFun) {
    $.ajax({
        url: url,
        dataType: 'json',
        type: 'get',
        data: data,
        complete: completeFun,
        success: function(jsonData) {
            if (!jsonData.result) {
                if (jsonData.errorCode == -2000) {
                    //没有权限访问时就跳转到登录页面
                    top.location.href = pageContextPath + "/login.html";
                } else if (jsonData.errorCode == 0) {
                    alert("对不起，系统正忙请您稍后重试!");
                } else {
                    succssFun(jsonData);
                }
            } else {
                succssFun(jsonData);
            }
        },
        error: function(jsonData) {
            alert("对不起，系统正忙请您稍后重试!");
        }
    });
}

/**
 * 获取url中的参数
 * @param paras
 */
function getURLParams(paras) {
    //   var url = location.href;
    //   var url = "http://192.168.1.73:9220/paoms/index.html?testParam=123";
    var url = window.location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {};
    for (i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];

    if (typeof(returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }

}

/**
 * 使用AJAX提交表单
 */
function submitForm(formId, url, successFun, errorFun) {
    $(formId).formSerialize();
    var options = {
        url: url,
        type: 'POST',
        dataType: 'json',
        success: function(jsonData) {
            if (!jsonData.result) {
                if (jsonData.errorCode == -2000) {
                    //没有权限访问时就跳转到登录页面
                    top.location.href = pageContextPath + "/login.html";
                } else if (jsonData.errorCode == 0) {
                    alert("对不起，系统正忙请您稍后重试!");
                } else {
                    errorFun(jsonData);
                }
            } else {
                successFun(jsonData);
            }
        }
    };

    $(formId).ajaxSubmit(options);
    return false;
}

//trim函数
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

//年月日
function dateToStr(date) {
    var date0 = date || new Date();
    var year = date0.getFullYear();
    var month = date0.getMonth() + 1;
    var day = date0.getDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    var date_str = year + "-" + month + "-" + day;
    return date_str;
}

//年月日时分秒
function dateToString(date) {
    var date0 = date || new Date();
    var year = date0.getFullYear();
    var month = date0.getMonth() + 1;
    var day = date0.getDate();
    var hour = date0.getHours();
    var minute = date0.getMinutes();
    var second = date0.getSeconds();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    if (hour < 10) {
        hour = "0" + hour;
    }
    if (minute < 10) {
        minute = "0" + minute;
    }
    if (second < 10) {
        second = "0" + second;
    }
    var date_str = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    return date_str;
}

function getUrlParam(param) {
    var reg = new RegExp("(^|&)" + param + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return (r[2]);
    return null;
}

//$(function (){
//	//点击全选
//	$("#checkbox_").live("click",function (){
//		if ($(this).prop("checked")) {
//			$(".checkbox_").prop("checked", true);
//		} else {
//			$(".checkbox_").prop("checked", false);
//		}
//	});
//	//点击单选
//	$(".checkbox_").live("click",function (){
//		if ($(".checkbox_").length == $(".checkbox_:checked").length) {
//			$("#checkbox_").prop("checked", true);
//		} else {
//			$("#checkbox_").prop("checked", false);
//		}
//	});
//});