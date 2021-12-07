package com.bl.nop.nis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface EasyArService {
	/**
	 * 获取token
	 * 
	 * @param pcId
	 * @return
	 */
	public JSONObject token();


	/**
	 * 识别日志存储
	 * 
	 * @param params
	 * @return
	 */
	public JSONObject savelog(Map<String, Object> params);

	/**
	 * 识别日志列表
	 * 
	 * @param params
	 * @return
	 */
    public JSONObject list(Map<String, Object> params);

}
