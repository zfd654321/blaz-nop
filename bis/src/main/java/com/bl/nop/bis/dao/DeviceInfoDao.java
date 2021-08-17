package com.bl.nop.bis.dao;

import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;

import org.springframework.stereotype.Repository;

@Repository
public interface DeviceInfoDao {

	public Device getOnlineDeviceByPcId(String sId);

    public DevicePc getDevicePcById(String sId);
	

}
