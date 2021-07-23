package com.bl.nop.tcs.util;

import com.alibaba.fastjson.JSONObject;

public class JsonResult {
	public static JSONObject success() 
	{
		JSONObject map = new JSONObject();
		map.put("code", 0);
		return map;
	}

	public static JSONObject success(Object data) 
	{
		JSONObject map = new JSONObject();
		map.put("code", 0);
		map.put("data", data);
		return map;
	}

	public static JSONObject fail(){
		JSONObject map = new JSONObject();
		map.put("code", -1);
		map.put("data", "error");
		return map;
	}
	
	public static JSONObject fail(String msg){
		JSONObject map = new JSONObject();
		map.put("code", -1);
		map.put("data", msg);
		return map;
	}
}
