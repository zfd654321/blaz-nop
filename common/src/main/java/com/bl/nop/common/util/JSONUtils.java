package com.bl.nop.common.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 异常或者返回为空时候的JSON工具类
 */
public class JSONUtils {
    
	public static void resultJSON(JSONObject oj,String return_code,JSONObject data_content,String return_msg){
		oj.put("return_code", return_code);
		oj.put("data_content", data_content);
		oj.put("return_msg", return_msg);
	}
}
