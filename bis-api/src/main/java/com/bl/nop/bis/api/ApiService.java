package com.bl.nop.bis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface ApiService {
	/**
	 * 根据软件码获取设备信息
	 * 
	 * @param pcId
	 * @return
	 */
	public JSONObject getOnlineDeviceByPcId(String pcId);


	/**
	 * 获取最新下载器信息
	 * 
	 * @return
	 */
	public JSONObject getDownLoader();


	/**
	 * 获取设备授权日期
	 * 
	 * @param params
	 * @return
	 */
	public JSONObject getDeviceOutDate(Map<String, Object> params);

}
