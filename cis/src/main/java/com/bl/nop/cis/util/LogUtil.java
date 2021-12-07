package com.bl.nop.cis.util;

import java.util.Date;

import com.bl.nop.entity.device.DeviceLog;

public class LogUtil {

	
	public static DeviceLog buildLog(String deviceId,Integer type, String operate, String info, Date createdAt, String createdBy) {
		DeviceLog log = new DeviceLog();
		log.setDeviceId(deviceId);
		log.setType(type);
		log.setOperate(operate);
		log.setInfo(info);
		log.setCreatedAt(createdAt);
		log.setCreatedBy(createdBy);
		return log;
	}

}
