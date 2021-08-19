package com.bl.nop.bis.api;

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

}
