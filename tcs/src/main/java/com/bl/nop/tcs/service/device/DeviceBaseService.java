package com.bl.nop.tcs.service.device;

import com.alibaba.fastjson.JSONObject;

public interface DeviceBaseService {
	/**
	 * 获取设备ID
	 * @param sId
	 * @return
	 */
	public JSONObject getDeviceID(String sId);
}
