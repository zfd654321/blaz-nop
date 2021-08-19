package com.bl.nop.bis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public interface DomainService {
	/**
	 * 对外提供api接口处理加密，参数
	 * @param params
	 * @return
	 */
	public JSONObject handle(Map<String, Object> params);
}
