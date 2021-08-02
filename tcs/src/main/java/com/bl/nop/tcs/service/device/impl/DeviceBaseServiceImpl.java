package com.bl.nop.tcs.service.device.impl;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.tcs.service.device.DeviceBaseService;

import org.springframework.stereotype.Service;

@Service("deviceBaseService")
public class DeviceBaseServiceImpl implements DeviceBaseService {

	
	@Override
	public JSONObject getDeviceID(String sId) {
		JSONObject oj = new JSONObject();
		JSONObject data_content = new JSONObject();
		oj.put("return_code", "1");
		oj.put("data_content", data_content);
		return oj;
	}

	
}
