package com.bl.nop.bis.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DomainService;
import com.bl.nop.bis.api.DownloadService;
import com.bl.nop.bis.dao.ConfigDao;
import com.bl.nop.bis.dao.DownloadDao;
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
import com.bl.nop.entity.device.DeviceSwitch;
import com.bl.nop.entity.game.Game;
import com.bl.nop.entity.version.Version;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("downloadService")
public class DownloadServiceImpl implements DownloadService {

	private final static Logger log = LoggerFactory.getLogger(ConfigServiceImpl.class);
	private final static String ERROR_CODE = "13";

	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private DomainService domainService;
	@Autowired
	private DeviceSwitchDao deviceSwitchDao;
	@Autowired
	private DeviceConfigDao deviceConfigDao;
	@Autowired
	private DownloadDao downloadDao;
	@Autowired
	private ConfigDao configDao;

	@Override
	public JSONObject version(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
		if (deviceSwitch == null || deviceSwitch.getSoftware() == 0) {
			log.info("设备更新开关已关闭");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "011", dataContent, "更新开关已关闭");
			return oj;
		}
		Device device = this.deviceDao.selectByPrimaryKey(deviceId);
		Map<String, Object> map = new HashMap<>();
		map.put("screen", device.getScreen());
		Version runVersion = this.downloadDao.queryRunVersion(map);
		if (runVersion == null) {
			log.info("没有可更新的版本");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "012", dataContent, "没有可更新的版本");
			return oj;
		}
		dataContent = FileUtil.getFileJson(runVersion.getFilePath() + "/file.json");
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject game(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		List<Game> games = this.configDao.queryDeviceGameList(deviceId);
		JSONArray list = new JSONArray();
		for (Game game : games) {
			String pathDir = PropertyUtil.getProperty("filePath") + "forever/game/";
			String gameJsonPath = pathDir + game.getId() + "/" + game.getVersion() + "/file.json";
			JSONObject gameJson = FileUtil.getFileJson(gameJsonPath);
			list.add(gameJson);
		}
		dataContent.put("list", list);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject resources(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");

		List<String> urlList = new ArrayList<>();
		// 设备配置中的2d资源
		DeviceConfig config = this.deviceConfigDao.selectByPrimaryKey(deviceId);
		if (urlList.indexOf(config.getLogoUrl()) == -1) {
			urlList.add(config.getLogoUrl());
		}
		if (urlList.indexOf(config.getQrcodeUrl()) == -1) {
			urlList.add(config.getQrcodeUrl());
		}
		if (urlList.indexOf(config.getWatermarkUrl()) == -1) {
			urlList.add(config.getWatermarkUrl());
		}
		// 广告中的2d资源
		List<AdvertDto> adverts = this.configDao.queryDeviceAdvertList(deviceId);
		for (AdvertDto advertDto : adverts) {
			String resource = advertDto.getResource();
			if (!StringUtils.isBlank(resource)) {
				String[] urls = resource.split(",");
				for (String url : urls) {
					if (urlList.indexOf(url) == -1) {
						urlList.add(url);
					}
				}
			}
		}
		// 游戏中的2d资源
		List<NetresDto> netres = this.configDao.queryDeviceGameNetresList(deviceId);
		for (NetresDto netresDto : netres) {
			String url = netresDto.getUrl();
			if (!StringUtils.isBlank(url) && urlList.indexOf(url) == -1) {
				urlList.add(url);
			}
		}
		// 读取资源列表md5
		JSONArray fileList = new JSONArray();
		try {
			for (String url : urlList) {
				JSONObject oj = new JSONObject();
				String path = url.replace(PropertyUtil.getProperty("fileUrl"), PropertyUtil.getProperty("filePath"));
				File file = new File(path);

				oj.put("name", file.getName());
				oj.put("md5", FileUtil.getMd5ByFile(file));
				oj.put("length", file.length());
				oj.put("url", url);
				fileList.add(oj);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataContent.put("fileList", fileList);
		return JSONUtils.success(dataContent);
	}

}
