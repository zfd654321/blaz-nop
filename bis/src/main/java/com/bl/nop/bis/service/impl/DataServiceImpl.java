package com.bl.nop.bis.service.impl;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DataService;
import com.bl.nop.bis.api.DomainService;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.data.DataErrorDao;
import com.bl.nop.dao.data.DataGameDao;
import com.bl.nop.dao.data.DataInfoDao;
import com.bl.nop.dao.data.DataOnlineDao;
import com.bl.nop.dao.device.DeviceSwitchDao;
import com.bl.nop.entity.data.DataError;
import com.bl.nop.entity.data.DataGame;
import com.bl.nop.entity.data.DataInfo;
import com.bl.nop.entity.data.DataOnline;
import com.bl.nop.entity.device.DeviceSwitch;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataService")
public class DataServiceImpl implements DataService {

	private final static Logger log = LoggerFactory.getLogger(DataServiceImpl.class);
	private final static String ERROR_CODE = "16";

	@Autowired
	private DomainService domainService;
	@Autowired
	private DataInfoDao dataInfoDao;
	@Autowired
	private DataGameDao dataGameDao;
	@Autowired
	private DataErrorDao dataErrorDao;
	@Autowired
	private DataOnlineDao dataOnlineDao;
	@Autowired
	private DeviceSwitchDao deviceSwitchDao;

	@Override
	public JSONObject heartData(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		String timeStr = StringUtil.toStr(params.get("dataTime"));
		DataOnline dataOnline = this.dataOnlineDao.selectByPrimaryKey(deviceId);
		if (dataOnline == null) {
			dataOnline = new DataOnline();
			dataOnline.setDeviceId(deviceId);
			this.dataOnlineDao.insert(dataOnline);
		}
		if (StringUtils.isBlank(timeStr)) {
			log.info("统计时间参数为空");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "011", dataContent, "统计时间参数为空");
			return oj;
		}
		Date dataTime = DateUtil.strToDateLong(timeStr, "yyyyMMddHHmmss");
		if (dataTime == null) {
			log.info("统计时间参数格式有误");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "012", dataContent, "统计时间参数格式有误");
			return oj;
		}
		Date lastHeart = dataOnline.getLastHeart();
		if (lastHeart != null && lastHeart.getTime() >= dataTime.getTime()) {
			log.info("统计时间不晚于上一次最后心跳");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "013", dataContent, "统计时间不晚于上一次最后心跳，请重新校验服务器时间");
			return oj;
		}
		Date now = new Date();
		long interval = (now.getTime() - dataTime.getTime()) / 1000;
		if (interval > 3600 || interval < -3600) {
			log.info("统计时间参数于服务器时间相差过大");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "014", dataContent, "统计时间参数于服务器时间相差过大，请重新校验服务器时间");
			return oj;
		}
		// 保存最近在线信息
		dataOnline.setLastHeart(dataTime);
		this.dataOnlineDao.updateByPrimaryKey(dataOnline);

		// 判断统计开关是否打开
		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
		if (deviceSwitch.getStatistics() == 0) {
			log.info("统计开关关闭，不进行相关统计");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "015", dataContent, "统计开关关闭，不进行相关统计");
			return oj;
		}

		Integer statsHour = DateUtil.getHourOfDay(dataTime);
		Integer usedDuration = NumberUtil.toInt(params.get("usedDuration"));
		Integer standDuration = NumberUtil.toInt(params.get("standDuration"));
		Integer personTime = NumberUtil.toInt(params.get("personTime"));
		Integer depthTime = NumberUtil.toInt(params.get("depthTime"));
		DataInfo dataInfo = new DataInfo();
		dataInfo.setDeviceId(deviceId);
		dataInfo.setDataTime(dataTime);
		dataInfo.setStatsHour(statsHour);
		dataInfo.setStatsDate(dataTime);
		dataInfo.setUsedDuration(usedDuration);
		dataInfo.setStandDuration(standDuration);
		dataInfo.setPersonTime(personTime);
		dataInfo.setDepthTime(depthTime);
		this.dataInfoDao.insert(dataInfo);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject gameData(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		DataOnline dataOnline = this.dataOnlineDao.selectByPrimaryKey(deviceId);
		if (dataOnline == null) {
			dataOnline = new DataOnline();
			dataOnline.setDeviceId(deviceId);
			this.dataOnlineDao.insert(dataOnline);
		}
		String gameId = StringUtil.toStr(params.get("gameId"));
		String beginTimeStr = StringUtil.toStr(params.get("beginTime"));
		String endTimeStr = StringUtil.toStr(params.get("endTime"));
		if (StringUtils.isBlank(beginTimeStr) || StringUtils.isBlank(endTimeStr)) {
			log.info("统计时间参数为空");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "011", dataContent, "游戏时间参数为空");
			return oj;
		}
		Date beginTime = DateUtil.strToDateLong(beginTimeStr, "yyyyMMddHHmmss");
		Date endTime = DateUtil.strToDateLong(endTimeStr, "yyyyMMddHHmmss");
		if (beginTime == null || endTime == null) {
			log.info("游戏时间参数格式有误");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "021", dataContent, "游戏时间参数格式有误");
			return oj;
		}
		if (beginTime.getTime() >= endTime.getTime()) {
			log.info("游戏结束时间需在开始时间之后");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "022", dataContent, "游戏结束时间需在开始时间之后");
			return oj;
		}
		Date lastGame = dataOnline.getLastGame();
		if (lastGame != null && lastGame.getTime() > beginTime.getTime()) {
			log.info("开始时间需晚于上一次游戏结束时间");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "023", dataContent, "开始时间需晚于上一次游戏结束时间");
			return oj;
		}
		// 判断统计开关是否打开
		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
		if (deviceSwitch.getStatistics() == 0) {
			log.info("统计开关关闭，不进行相关统计");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "024", dataContent, "统计开关关闭，不进行相关统计");
			return oj;
		}
		Integer type = NumberUtil.toInt(params.get("type"));
		Integer statsHour = DateUtil.getHourOfDay(endTime);
		DataGame dataGame = new DataGame();
		dataGame.setDeviceId(deviceId);
		dataGame.setStatsDate(endTime);
		dataGame.setStatsHour(statsHour);
		dataGame.setGameId(gameId);
		dataGame.setBeginTime(beginTime);
		dataGame.setEndTime(endTime);
		dataGame.setType(type);
		dataGame.setDuration(NumberUtil.toInt(endTime.getTime() - beginTime.getTime()) / 1000);
		this.dataGameDao.insert(dataGame);

		dataOnline.setLastGame(endTime);
		this.dataOnlineDao.updateByPrimaryKey(dataOnline);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject errorData(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		String errorMessage = StringUtil.toStr(params.get("errorMessage"));
		String errTimeStr = StringUtil.toStr(params.get("errorTime"));
		if (StringUtils.isBlank(errTimeStr)) {
			log.info("统计时间参数为空");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "011", dataContent, "报错时间参数为空");
			return oj;
		}
		Date errorTime = DateUtil.strToDateLong(errTimeStr, "yyyyMMddHHmmss");
		if (errorTime == null) {
			log.info("报错时间参数格式有误");
			JSONObject oj = JSONUtils.error(ERROR_CODE + "021", dataContent, "报错时间参数格式有误");
			return oj;
		}
		DataError dataError = new DataError();
		dataError.setDeviceId(deviceId);
		dataError.setErrorTime(errorTime);
		dataError.setErrorMessage(errorMessage);
		this.dataErrorDao.insert(dataError);
		return JSONUtils.success(dataContent);
	}

}
