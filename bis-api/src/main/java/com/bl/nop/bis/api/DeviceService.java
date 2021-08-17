package com.bl.nop.bis.api;

import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.version.VersionDownloader;

public interface DeviceService {
	/**
	 * 根据软件码获取设备信息
	 * 
	 * @param sId
	 * @return
	 */
	public Device getOnlineDeviceByPcId(String sId);

	/**
	 * 根据软件码获取PC信息
	 * 
	 * @param sId
	 * @return
	 */
	public DevicePc getDevicePcById(String sId);

	/**
	 * 获取最新下载器信息
	 * 
	 * @return
	 */
	public VersionDownloader getDownLoader();

}
