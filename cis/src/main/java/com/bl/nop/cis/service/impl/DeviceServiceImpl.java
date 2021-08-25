package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bl.nop.cis.api.DeviceService;
import com.bl.nop.cis.dao.OmsDeviceDao;
import com.bl.nop.cis.dto.GameNetresDto;
import com.bl.nop.cis.util.LogUtil;
import com.bl.nop.cis.util.PropertyUtil;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.device.DeviceAdvertDao;
import com.bl.nop.dao.device.DeviceConfigDao;
import com.bl.nop.dao.device.DeviceDao;
import com.bl.nop.dao.device.DeviceGameDao;
import com.bl.nop.dao.device.DeviceGameNetresDao;
import com.bl.nop.dao.device.DeviceLogDao;
import com.bl.nop.dao.device.DevicePcDao;
import com.bl.nop.dao.device.DeviceSwitchDao;
import com.bl.nop.entity.advert.Advert;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DeviceAdvert;
import com.bl.nop.entity.device.DeviceConfig;
import com.bl.nop.entity.device.DeviceGame;
import com.bl.nop.entity.device.DeviceGameNetres;
import com.bl.nop.entity.device.DeviceLog;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.device.DeviceSwitch;
import com.bl.nop.entity.game.Game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

	private final static Logger log = LoggerFactory.getLogger(DevicePcServiceImpl.class);

	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private DevicePcDao devicePcDao;
	@Autowired
	private DeviceConfigDao deviceConfigDao;
	@Autowired
	private DeviceSwitchDao deviceSwitchDao;
	@Autowired
	private DeviceAdvertDao deviceAdvertDao;
	@Autowired
	private DeviceGameDao deviceGameDao;
	@Autowired
	private DeviceGameNetresDao deviceGameNetresDao;
	@Autowired
	private DeviceLogDao deviceLogDao;
	@Autowired
	private OmsDeviceDao omsDeviceDao;

	private static final String ERROR_CODE = "022";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<Device> page = new Page<Device>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsDeviceDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<Device> list = this.omsDeviceDao.findItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		boolean edit = StringUtil.toStr(params.get("edit")).equals("true");
		String pcId = StringUtil.toStr(params.get("pcId"));
		String name = StringUtil.toStr(params.get("name"));
		String remarks = StringUtil.toStr(params.get("remarks"));
		String address = StringUtil.toStr(params.get("address"));
		Integer type = NumberUtil.toInt(params.get("type"));
		Integer screen = NumberUtil.toInt(params.get("screen"));
		String camera = StringUtil.toStr(params.get("camera"));
		String outDate = StringUtil.toStr(params.get("outDate"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Device reitem = this.deviceDao.selectByPrimaryKey(deviceId);
		if (edit) {// 修改
			Device item = reitem;
			if (!pcId.equals(reitem.getPcId())) {// 更换PC
				DevicePc repc = this.devicePcDao.selectByPrimaryKey(reitem.getPcId());
				DevicePc pc = this.devicePcDao.selectByPrimaryKey(pcId);
				if (pc.getStatus() != 1) {
					return ResResultBean.error(ERROR_CODE + "003", "该机器码状态不为空闲，请更换机器码");
				}
				if (repc != null) {
					repc.setStatus(1);
					this.devicePcDao.updateByPrimaryKey(repc);
				}
				pc.setStatus(2);
				item.setPcId(pcId);
				this.devicePcDao.updateByPrimaryKey(pc);

			}
			item.setName(name);
			item.setRemarks(remarks);
			item.setAddress(address);
			item.setType(type);
			item.setScreen(screen);
			item.setCamera(camera);
			item.setOutDate(DateUtil.strToDateShort(outDate));
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.deviceDao.updateByPrimaryKey(item);
			DeviceLog devicelog = LogUtil.buildLog(deviceId, "设备信息修改", JSONObject.toJSONString(item), now, createdBy);
			this.deviceLogDao.insert(devicelog);

		} else {// 新增
			log.info("新增设备>>>>>deviceId:" + deviceId);
			if (reitem != null) {
				return ResResultBean.error(ERROR_CODE + "002", "该设备编号已存在，请勿重复添加");
			}
			DevicePc pc = this.devicePcDao.selectByPrimaryKey(pcId);
			if (pc.getStatus() != 1) {
				return ResResultBean.error(ERROR_CODE + "003", "该机器码状态不为空闲，请更换机器码");
			}

			Device item = new Device();
			item.setDeviceId(deviceId);
			item.setPcId(pcId);
			item.setName(name);
			item.setRemarks(remarks);
			item.setAddress(address);
			item.setType(type);
			item.setScreen(screen);
			item.setCamera(camera);
			item.setStatus(1);
			item.setOutDate(DateUtil.strToDateShort(outDate));
			item.setCreatedAt(now);
			item.setCreatedBy(createdBy);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.deviceDao.insert(item);
			pc.setStatus(2);
			this.devicePcDao.updateByPrimaryKey(pc);
			DeviceLog devicelog = LogUtil.buildLog(deviceId, "设备新增", JSONObject.toJSONString(item), now, createdBy);
			this.deviceLogDao.insert(devicelog);
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean freePcList(Map<String, Object> params) {
		List<DevicePc> list = this.omsDeviceDao.freePcList(params);
		return ResResultBean.success(list);
	}

	@Override
	public ResResultBean intoHouse(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Device device = this.deviceDao.selectByPrimaryKey(deviceId);
		device.setStatus(2);
		device.setUpdatedAt(now);
		device.setUpdatedBy(createdBy);
		this.deviceDao.updateByPrimaryKey(device);

		DeviceConfig deviceConfig = this.deviceConfigDao.selectByPrimaryKey(deviceId);
		if (deviceConfig == null) {
			deviceConfig = new DeviceConfig();
			deviceConfig.setDeviceId(deviceId);
			deviceConfig.setKinectLeftAndRightDis(PropertyUtil.getProperty("kinectLeftAndRightDis"));
			deviceConfig.setKinectMinDis(PropertyUtil.getProperty("kinectMinDis"));
			deviceConfig.setKinectMaxDis(PropertyUtil.getProperty("kinectMaxDis"));
			deviceConfig.setHostUrl(PropertyUtil.getProperty("hostUrl"));
			deviceConfig.setQrcodeUrl(PropertyUtil.getProperty("qrcodeUrl"));
			deviceConfig.setLogoUrl(PropertyUtil.getProperty("logoUrl"));
			deviceConfig.setWatermarkUrl(PropertyUtil.getProperty("watermarkUrl"));
			deviceConfig.setThemeName(PropertyUtil.getProperty("themeName"));
			deviceConfig.setGameEnterTime(PropertyUtil.getProperty("gameEnterTime"));
			this.deviceConfigDao.insert(deviceConfig);
		}

		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
		if (deviceSwitch == null) {
			deviceSwitch = new DeviceSwitch();
			deviceSwitch.setDeviceId(deviceId);
			deviceSwitch.setSoftware(1);
			deviceSwitch.setPay(0);
			deviceSwitch.setStatistics(1);
			deviceSwitch.setOnlinecheck(0);
			deviceSwitch.setFilecheck(0);
			this.deviceSwitchDao.insert(deviceSwitch);
		}

		DeviceLog devicelog = LogUtil.buildLog(deviceId, "设备入库", "", now, createdBy);
		this.deviceLogDao.insert(devicelog);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean updateStatus(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		Integer status = NumberUtil.toInt(params.get("status"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Device device = this.deviceDao.selectByPrimaryKey(deviceId);
		device.setStatus(status);
		device.setUpdatedAt(now);
		device.setUpdatedBy(createdBy);
		this.deviceDao.updateByPrimaryKey(device);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Device device = this.deviceDao.selectByPrimaryKey(deviceId);
		if (device != null) {
			DevicePc pc = this.devicePcDao.selectByPrimaryKey(device.getPcId());
			if (pc != null && pc.getStatus() == 2) {
				pc.setStatus(1);
				this.devicePcDao.updateByPrimaryKey(pc);
			}
			this.deviceDao.deleteByPrimaryKey(deviceId);
		}
		DeviceLog devicelog = LogUtil.buildLog(deviceId, "设备删除", "", now, createdBy);
		this.deviceLogDao.insert(devicelog);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadDeviceConfig(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		DeviceConfig deviceConfig = this.deviceConfigDao.selectByPrimaryKey(deviceId);
		return ResResultBean.success(deviceConfig);
	}

	@Override
	public ResResultBean saveDeviceConfig(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		DeviceConfig deviceConfig = JSON.parseObject(JSON.toJSONString(params), DeviceConfig.class);
		this.deviceConfigDao.updateByPrimaryKey(deviceConfig);
		DeviceLog devicelog = LogUtil.buildLog(deviceConfig.getDeviceId(), "设备配置修改",
				JSONObject.toJSONString(deviceConfig), now, createdBy);
		this.deviceLogDao.insert(devicelog);

		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadDeviceAdvert(Map<String, Object> params) {
		List<Advert> list = this.omsDeviceDao.deviceAdvertList(params);
		return ResResultBean.success(list);
	}

	@Override
	public ResResultBean saveDeviceAdvert(Map<String, Object> params) {
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		String adverts = StringUtil.toStr(params.get("adverts"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		this.omsDeviceDao.cleanDeviceAdvert(deviceId);
		String[] advertList = adverts.split(",");
		for (int i = 0; i < advertList.length; i++) {
			String advertId = advertList[i];
			DeviceAdvert deviceAdvert = new DeviceAdvert();
			deviceAdvert.setDeviceId(deviceId);
			deviceAdvert.setAdvertId(advertId);
			deviceAdvert.setWeight(i);
			this.deviceAdvertDao.insert(deviceAdvert);
		}

		DeviceLog devicelog = LogUtil.buildLog(deviceId, "设备广告修改", adverts, now, createdBy);
		this.deviceLogDao.insert(devicelog);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadDeviceGame(Map<String, Object> params) {
		List<Game> list = this.omsDeviceDao.deviceGameList(params);
		return ResResultBean.success(list);
	}

	@Override
	public ResResultBean loadDeviceGameNetres(Map<String, Object> params) {
		List<GameNetresDto> list = this.omsDeviceDao.deviceGameNetresList(params);
		return ResResultBean.success(list);
	}

	@Override
	public ResResultBean saveDeviceGame(Map<String, Object> params) {
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		String gameIds = StringUtil.toStr(params.get("gameIds"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();

		this.omsDeviceDao.cleanDeviceGame(deviceId);
		this.omsDeviceDao.cleanDeviceGameNetres(deviceId);
		String[] gameIdList = gameIds.split(",");
		Integer i = 0;
		for (String gameId : gameIdList) {
			DeviceGame game = new DeviceGame();
			game.setDeviceId(deviceId);
			game.setGameId(gameId);
			game.setWeight(i++);
			this.deviceGameDao.insert(game);
		}

		String slist = StringUtil.toStr(params.get("slist"));
		JSONArray jsonArray = JSONArray.parseArray(slist);
		for (Object object : jsonArray) {
			JSONObject obj = (JSONObject) object;
			String gameId = obj.getString("gameId");
			String property = obj.getString("property");
			String resUrl = obj.getString("resUrl");
			DeviceGameNetres netres = new DeviceGameNetres();
			netres.setDeviceId(deviceId);
			netres.setGameId(gameId);
			netres.setProperty(property);
			netres.setResUrl(resUrl);
			this.deviceGameNetresDao.insert(netres);
		}
		JSONObject logoj = new JSONObject();
		logoj.put("gameIds", gameIds);
		logoj.put("netres", jsonArray);
		DeviceLog devicelog = LogUtil.buildLog(deviceId, "设备游戏修改", JSONObject.toJSONString(logoj), now, createdBy);
		this.deviceLogDao.insert(devicelog);

		return ResResultBean.success();
	}

	@Override
	public ResResultBean deviceCopy(Map<String, Object> params) {
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		String[] checkDevices = StringUtil.toStr(params.get("checkDevices[]")).split(",");
		String[] checkType = StringUtil.toStr(params.get("checkType[]")).split(",");
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		JSONObject logoj = new JSONObject();
		logoj.put("fromDeviceId", deviceId);
		logoj.put("toDeviceIds", checkDevices);
		logoj.put("copyTypes", checkType);
		for (String toDeviceId : checkDevices) {
			for (String type : checkType) {
				switch (type) {
					case "1":
						// 配置平移
						DeviceConfig config = this.deviceConfigDao.selectByPrimaryKey(deviceId);
						config.setDeviceId(toDeviceId);
						this.deviceConfigDao.updateByPrimaryKey(config);

						break;
					case "2":
						// 开关平移
						DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
						deviceSwitch.setDeviceId(toDeviceId);
						this.deviceSwitchDao.updateByPrimaryKey(deviceSwitch);

						break;
					case "3":
						// 广告平移
						List<DeviceAdvert> list = this.omsDeviceDao.queryDeviceAdvert(params);
						this.omsDeviceDao.cleanDeviceAdvert(toDeviceId);
						for (DeviceAdvert advert : list) {
							advert.setDeviceId(toDeviceId);
							this.deviceAdvertDao.insert(advert);
						}

						break;
					case "4":
						// 游戏平移
						List<DeviceGame> games = this.omsDeviceDao.queryDeviceGame(params);
						List<DeviceGameNetres> netres = this.omsDeviceDao.queryDeviceGameNetres(params);
						this.omsDeviceDao.cleanDeviceGame(toDeviceId);
						this.omsDeviceDao.cleanDeviceGameNetres(toDeviceId);
						for (DeviceGame game : games) {
							game.setDeviceId(toDeviceId);
							this.deviceGameDao.insert(game);
						}
						for (DeviceGameNetres netre : netres) {
							netre.setDeviceId(toDeviceId);
							this.deviceGameNetresDao.insert(netre);
						}

						break;
					default:
						break;
				}
			}
			DeviceLog devicelog = LogUtil.buildLog(toDeviceId, "设备配置平移-被移入，从[" + deviceId + "]",
					JSONObject.toJSONString(logoj), now, createdBy);
			this.deviceLogDao.insert(devicelog);

		}

		DeviceLog devicelog = LogUtil.buildLog(deviceId, "设备配置平移-被移出", JSONObject.toJSONString(logoj), now, createdBy);
		this.deviceLogDao.insert(devicelog);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean deviceOnlineCheck(Map<String, Object> params) {
		List<Advert> advertList = this.omsDeviceDao.deviceAdvertList(params);
		List<Game> gameList = this.omsDeviceDao.deviceGameList(params);
		if (advertList.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "011", "设备尚未配置广告位，无法上线");
		}
		if (gameList.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "012", "设备尚未配置游戏，无法上线");
		}
		return ResResultBean.success();
	}

}
