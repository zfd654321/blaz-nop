package com.bl.nop.bis.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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
			return JSONUtils.error(ERROR_CODE + "012", dataContent, "没有可更新的版本");
		}
		dataContent = FileUtil.getFileJson(runVersion.getFilePath() + "/file.json");
		if (dataContent.isEmpty()) {
			return JSONUtils.error(ERROR_CODE + "013", dataContent, "版本资源列表文件丢失" + runVersion.getId());
		}
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
			log.info(JSONObject.toJSONString(game));
			String pathDir = PropertyUtil.getProperty("filePath") + "forever/game/";
			String gameJsonPath = pathDir + game.getId() + "/" + game.getVersion() + "/file.json";
			JSONObject gameJson = FileUtil.getFileJson(gameJsonPath);
			if (gameJson.isEmpty()) {
				return JSONUtils.error(ERROR_CODE + "021", dataContent, "游戏资源列表文件丢失" + game.getId());
			}
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
		if (config == null) {
			return JSONUtils.error(ERROR_CODE + "031", dataContent, "设备配置出错");
		}
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
		if (adverts.isEmpty()) {
			return JSONUtils.error(ERROR_CODE + "032", dataContent, "广告配置出错");
		}
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
		List<String> batchFile = new ArrayList<>();
		for (NetresDto netresDto : netres) {
			String url = netresDto.getUrl();
			if (!StringUtils.isBlank(url) && urlList.indexOf(url) == -1) {
				urlList.add(url);
				if ("filejson".equals(netresDto.getType())) {
					batchFile.add(url);
				}
			}
		}
		// 读取资源列表md5
		JSONArray fileList = new JSONArray();
		String fileUrl=PropertyUtil.getProperty("fileUrl");
		String filePath=PropertyUtil.getProperty("filePath");
		try {
			log.info("resourceCount:"+urlList.size());
			for (String url : urlList) {
				log.info("url:"+url);
				JSONObject oj = new JSONObject();
				String path = url.replace(fileUrl, filePath);
				File file = new File(path);
				if (!file.exists()) {
					return JSONUtils.error(ERROR_CODE + "033", dataContent, "2d资源文件丢失" + url);
				}
				oj.put("name", file.getName());
				oj.put("md5", FileUtil.getMd5ByFile(file));
				oj.put("length", file.length());
				oj.put("url", url);
				fileList.add(oj);
			}
			//载入filejson内资源
			for (String urlPath : batchFile) {
				File jsonFile = new File(urlPath.replace(fileUrl, filePath));
				String jsonStr = "";
				try {
					FileReader fileReader = new FileReader(jsonFile);
					Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
					int ch = 0;
					StringBuffer sb = new StringBuffer();
					while ((ch = reader.read()) != -1) {
						sb.append((char) ch);
					}
					fileReader.close();
					reader.close();
					jsonStr = sb.toString();
					log.info("————读取" + urlPath + "文件结束!————");
				} catch (Exception e) {
					log.info("————读取" + urlPath + "文件出现异常，读取失败!————");
					e.printStackTrace();
				}
				JSONObject jsonObject = JSONObject.parseObject(jsonStr);
				JSONArray jsonArray = jsonObject.getJSONArray("fileList");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject reoj = jsonArray.getJSONObject(i);
						JSONObject oj = new JSONObject();
						
						log.info("filejson-url:"+reoj.getString("network_url"));
						oj.put("name", reoj.getString("name"));
						oj.put("md5", reoj.getString("md5"));
						oj.put("length", reoj.getString("file_length"));
						oj.put("url", reoj.getString("network_url"));
						fileList.add(oj);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataContent.put("fileList", fileList);
		return JSONUtils.success(dataContent);
	}

}
