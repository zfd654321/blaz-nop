package com.bl.nop.tcs.service.device.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DeviceService;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.entity.device.Device;
import com.bl.nop.tcs.service.device.DeviceBaseService;

@Service("deviceBaseService")
public class DeviceBaseServiceImpl implements DeviceBaseService {

	private static Logger log = LoggerFactory.getLogger(DeviceBaseServiceImpl.class);
	
	@Autowired
	private DeviceService deviceService;
	
	private final static String ERROR_CODE = "12";
	@Override
	public JSONObject getDeviceID(String sId) {
		JSONObject oj = new JSONObject();
		JSONObject data_content = new JSONObject();
		oj.put("return_code", "1");
		oj.put("data_content", data_content);
		return oj;
	}

	
}
