package com.bl.nop.nis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface DomainService {
	/**
	 * 根据软件码获取设备信息
	 * 
	 * @param pcId
	 * @return
	 */
	public JSONObject getWXUserOpenId(String code);

    public JSONObject userData(Map<String, Object> params);


}
