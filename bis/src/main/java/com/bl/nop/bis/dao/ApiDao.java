package com.bl.nop.bis.dao;

import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.version.VersionDownloader;

import org.springframework.stereotype.Repository;

@Repository
public interface ApiDao {

	public Device getOnlineDeviceByPcId(String sId);

    public DevicePc getDevicePcById(String sId);

    public VersionDownloader getDownLoader(String sId);
	

}
