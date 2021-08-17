package com.bl.nop.tcs.service.device.impl;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DeviceService;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.version.VersionDownloader;
import com.bl.nop.tcs.service.device.DeviceBaseService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("deviceBaseService")
public class DeviceBaseServiceImpl implements DeviceBaseService {

	@Autowired
	private DeviceService deviceService;

	private final static String ERROR_CODE = "12";

	@Override
	public JSONObject getDeviceID(String sId) {
		JSONObject oj = new JSONObject();
		JSONObject data_content = new JSONObject();
		if (StringUtils.isEmpty(sId)) {
			oj.put("return_code", ERROR_CODE + "011");
			oj.put("return_msg", "参数s_id为空:" + sId);
			return oj;
		}

		Device device=deviceService.getOnlineDeviceByPcId(sId);

		if (device == null) {
			oj.put("return_code", ERROR_CODE+"012");
			oj.put("return_msg", "设备未上线");
			return oj;
		}

		String device_idMD5 = Md5Util.toMd5(device.getDeviceId() + "BLAZ");
		DevicePc pc=deviceService.getDevicePcById(sId);

		data_content.put("device_idMD5", device_idMD5);
		data_content.put("activate_file", pc.getLicense());
		oj.put("return_code", "1");
		oj.put("data_content", data_content);
		return oj;
	}

	@Override
	public JSONObject getDownloader() {
		JSONObject oj = new JSONObject();
		JSONObject data_content = new JSONObject();
		VersionDownloader downloader=deviceService.getDownLoader();
		data_content.put("version", downloader.getId());
		data_content.put("url", downloader.getUrl());
		data_content.put("md5", downloader.getMd5());

		oj.put("return_code", "1");
		oj.put("data_content", data_content);
		return oj;
	}

}
