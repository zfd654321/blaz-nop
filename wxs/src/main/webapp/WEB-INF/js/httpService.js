var pageContextPath='/tcs';
/**

* 使用post方式向后台发送请求
 * @param url 请求的URL
 * @param data 请求的参数
 * @param succssFun  加载成功后的函数
 */
function sendRequest(url, data, succssFun,asy) {
    if(asy==undefined || asy==true){
        asy = true;
    }
    $.ajax({
        url: url ,
        dataType: 'json',
        type:'post',
        data:data,
        async:asy,
        success: function(jsonData) {
            //将后台响应的数据转换成json数据
        	succssFun(jsonData);
        },
        error:function(jsonData) {
            //alert("对不起，系统正忙请您稍后重试!");
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
        url: url ,
        dataType: 'json',
        type:'get',
        data:data,
        complete:completeFun ,
        success: function(jsonData) {
            succssFun(jsonData);
        },
        error:function(jsonData) {
            //alert("对不起，系统正忙请您稍后重试!");
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
        url:url,
        type:'POST',
        dataType:'json',
        success : function(jsonData) {
            if (!jsonData.result) {
            	errorFun(jsonData);
            } else {
                successFun(jsonData);
            }
        }
    };

    $(formId).ajaxSubmit(options);
    return false;
}

//trim函数
String.prototype.trim = function()  
{  
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

