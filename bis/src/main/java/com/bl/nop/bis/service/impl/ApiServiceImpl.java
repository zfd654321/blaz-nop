package com.bl.nop.bis.service.impl;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.ApiService;
import com.bl.nop.bis.api.DomainService;
import com.bl.nop.bis.dao.ApiDao;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.dao.device.DeviceDao;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.version.VersionDownloader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiService")
public class ApiServiceImpl implements ApiService {

	private final static Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
	private final static String ERROR_CODE = "12";

	@Autowired
	private ApiDao apiDao;
	@Autowired
	private DomainService domainService;
	@Autowired
	private DeviceDao deviceDao;

	@Override
	public JSONObject getOnlineDeviceByPcId(String pcId) {
		JSONObject dataContent = new JSONObject();
		if (StringUtils.isEmpty(pcId)) {
			log.info("参数pcId为空");
			return JSONUtils.error(ERROR_CODE + "011", dataContent, "参数pcId为空");
		}
		log.info("根据软件码[" + pcId + "]获取设备信息");
		Device device = apiDao.getOnlineDeviceByPcId(pcId);
		if (device == null) {
			log.info("设备未上线");
			return JSONUtils.error(ERROR_CODE + "012", dataContent, "设备未上线");
		}
		String device_idMd5 = Md5Util.toMd5(device.getDeviceId() + "BLAZ");
		DevicePc pc = apiDao.getDevicePcById(pcId);
		dataContent.put("deviceIdMd5", device_idMd5);
		dataContent.put("activateFile", pc.getLicense());
		JSONObject oj = JSONUtils.success(dataContent);
		return oj;
	}

	@Override
	public JSONObject getDownLoader() {
		JSONObject dataContent = new JSONObject();
		VersionDownloader downloader = apiDao.getDownLoader("");
		if (null == downloader) {
			log.info("没有适配的下载器");
			return JSONUtils.error(ERROR_CODE + "021", dataContent, "没有适配的下载器");
		}
		dataContent.put("version", downloader.getId());
		dataContent.put("url", downloader.getUrl());
		dataContent.put("md5", downloader.getMd5());

		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject getDeviceOutDate(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		Device device = this.deviceDao.selectByPrimaryKey(deviceId);
		dataContent.put("outDate", DateUtil.dateToStrShort(device.getOutDate()));
		return JSONUtils.success(dataContent);
	}

}
