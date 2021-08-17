package com.bl.nop.bis.service.impl;

import com.bl.nop.bis.api.DeviceService;
import com.bl.nop.bis.dao.DeviceInfoDao;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.version.VersionDownloader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

	private final static Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

	@Autowired
	private DeviceInfoDao deviceInfoDao;

	@Override
	public Device getOnlineDeviceByPcId(String sId) {
		if (StringUtils.isBlank(sId)) {
			log.info("根据软件码获取设备信息，软件码为空");
			return null;
		}
		log.info("根据软件码[" + sId + "]获取设备信息");
		Device onlineDevice = deviceInfoDao.getOnlineDeviceByPcId(sId);
		return onlineDevice;
	}

	@Override
	public DevicePc getDevicePcById(String sId) {
		if (StringUtils.isBlank(sId)) {
			log.info("根据软件码获取PC信息，软件码为空");
			return null;
		}
		log.info("根据软件码[" + sId + "]获取设备信息");
		DevicePc pc = deviceInfoDao.getDevicePcById(sId);
		return pc;
	}

	@Override
	public VersionDownloader getDownLoader() {
		VersionDownloader downloader=deviceInfoDao.getDownLoader("");

		return downloader;
	}

}
