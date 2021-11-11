package com.bl.nop.bis.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.ConfigService;
import com.bl.nop.bis.api.DomainService;
import com.bl.nop.bis.dao.ConfigDao;
import com.bl.nop.bis.dto.AdvertDto;
import com.bl.nop.bis.dto.NetresDto;
import com.bl.nop.bis.util.FileUtil;
import com.bl.nop.bis.util.PropertyUtil;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.dao.device.DeviceConfigDao;
import com.bl.nop.dao.device.DeviceDao;
import com.bl.nop.dao.device.DeviceSwitchDao;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DeviceConfig;
import com.bl.nop.entity.device.DeviceRequest;
import com.bl.nop.entity.device.DeviceSwitch;
import com.bl.nop.entity.game.Game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

	private final static Logger log = LoggerFactory.getLogger(ConfigServiceImpl.class);
	private final static String ERROR_CODE = "14";

	@Autowired
	private DomainService domainService;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private DeviceSwitchDao deviceSwitchDao;
	@Autowired
	private DeviceConfigDao deviceConfigDao;

	@Autowired
	private ConfigDao configDao;

	@Override
	public JSONObject list(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
		if (deviceSwitch == null || deviceSwitch.getSoftware() == 0) {
			log.info("设备更新开关已关闭");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "012", dataContent, "更新开关已关闭");
			return oj;
		}
		List<DeviceRequest> list = this.configDao.queryDeviceRequestList();
		dataContent.put("list", list);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject deviceConfig(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		Device device = this.deviceDao.selectByPrimaryKey(deviceId);
		DeviceConfig config = this.deviceConfigDao.selectByPrimaryKey(deviceId);
		if (config == null) {
			log.info("设备配置出错");
			return JSONUtils.error(ERROR_CODE + "021", dataContent, "设备配置出错");
		}
		dataContent.put("screen", device.getScreen());
		dataContent.put("kinectLeftAndRightDis", config.getKinectLeftAndRightDis());
		dataContent.put("kinectMinDis", config.getKinectMinDis());
		dataContent.put("kinectMaxDis", config.getKinectMaxDis());
		dataContent.put("hostUrl", config.getHostUrl());
		dataContent.put("qrcodeUrl", config.getQrcodeUrl());
		dataContent.put("logoUrl", config.getLogoUrl());
		dataContent.put("watermarkUrl", config.getWatermarkUrl());
		dataContent.put("themeName", config.getThemeName());
		dataContent.put("gameEnterTime", config.getGameEnterTime());
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject deviceSwitch(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
		if (deviceSwitch == null) {
			log.info("设备开关配置出错");
			return JSONUtils.error(ERROR_CODE + "031", dataContent, "设备开关配置出错");
		}
		dataContent.put("software", deviceSwitch.getSoftware());
		dataContent.put("pay", deviceSwitch.getPay());
		dataContent.put("statistics", deviceSwitch.getStatistics());
		dataContent.put("filecheck", deviceSwitch.getFilecheck());
		dataContent.put("onlinecheck", deviceSwitch.getOnlinecheck());
		dataContent.put("video", deviceSwitch.getVideo());
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject advert(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		List<AdvertDto> list = this.configDao.queryDeviceAdvertList(deviceId);
		if (list.isEmpty()) {
			log.info("设备广告配置出错");
			return JSONUtils.error(ERROR_CODE + "041", dataContent, "设备广告配置出错");
		}
		dataContent.put("list", list);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject game(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		List<Game> games = this.configDao.queryDeviceGameList(deviceId);
		if (games.isEmpty()) {
			log.info("设备游戏配置出错");
			return JSONUtils.error(ERROR_CODE + "051", dataContent, "设备游戏配置出错");
		}
		JSONArray list = new JSONArray();
		for (Game game : games) {
			String pathDir = PropertyUtil.getProperty("filePath") + "forever/game/";
			String gameJsonPath = pathDir + game.getId() + "/" + game.getVersion() + "/game.json";
			JSONObject gameJson = FileUtil.getFileJson(gameJsonPath);
			if (gameJson.isEmpty()) {
				log.info("设备游戏配置表丢失");
				return JSONUtils.error(ERROR_CODE + "052", dataContent, "设备游戏配置表丢失" + game.getId());
			}
			list.add(gameJson);
		}
		dataContent.put("list", list);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject gameNetres(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		List<NetresDto> netreList = this.configDao.queryDeviceGameNetresList(deviceId);
		dataContent.put("list", netreList);
		return JSONUtils.success(dataContent);
	}

}
