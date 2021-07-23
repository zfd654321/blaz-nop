package com.bl.nop.common.util;

import com.alibaba.fastjson.JSONObject;

public class JSONObjectUtil {

	private JSONObject jsonObject;
	
	private JSONObjectUtil() {
		jsonObject = new JSONObject();
	}
	
	public static JSONObjectUtil create() {
		JSONObjectUtil jsonObjectUtil = new JSONObjectUtil();
		return jsonObjectUtil;
	}
	
	public static JSONObjectUtil create(String key, Object value) {
		JSONObjectUtil jsonObjectUtil = new JSONObjectUtil();
		jsonObjectUtil.put(key, value);
		return jsonObjectUtil;
	}
	
	public JSONObjectUtil put(String key, Object value) {
		this.jsonObject.put(key, value);
		return this;
	}
	
	public JSONObject toJson() {
		return this.jsonObject;
	}
}
