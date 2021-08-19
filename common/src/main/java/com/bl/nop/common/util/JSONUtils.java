package com.bl.nop.common.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 异常或者返回为空时候的JSON工具类
 */
public class JSONUtils {
    
	public static JSONObject error(String returnCode,JSONObject dataContent,String returnMsg){
		JSONObject oj = new JSONObject();
		oj.put("returnCode", returnCode);
		oj.put("dataContent", dataContent);
		oj.put("returnMsg", returnMsg);
		return oj;
	}

	public static JSONObject success(JSONObject dataContent){
		JSONObject oj = new JSONObject();
		oj.put("returnCode", "1");
		oj.put("dataContent", dataContent);
		return oj;
	}
}
