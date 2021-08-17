package com.bl.nop.bis.api;

import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;

public interface DeviceService {
	/**
	 * 根据软件码获取设备信息
	 * @param sId
	 * @return
	 */
    public Device getOnlineDeviceByPcId(String sId);

	public DevicePc getDevicePcById(String sId);
	
}
