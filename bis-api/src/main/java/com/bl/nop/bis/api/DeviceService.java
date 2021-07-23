package com.bl.nop.bis.api;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.entity.device.Device;

public interface DeviceService {
	/**
	 * 根据软件码获取设备信息
	 * @param sId
	 * @return
	 */
	public Device getDeviceBySId(String sId);
	
}
