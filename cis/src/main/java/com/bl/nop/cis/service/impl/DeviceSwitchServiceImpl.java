package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.cis.api.DeviceSwitchService;
import com.bl.nop.cis.dao.OmsDeviceSwitchDao;
import com.bl.nop.cis.dto.SwitchDto;
import com.bl.nop.cis.util.LogUtil;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.device.DeviceLogDao;
import com.bl.nop.dao.device.DeviceSwitchDao;
import com.bl.nop.entity.device.DeviceLog;
import com.bl.nop.entity.device.DeviceSwitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("deviceSwitchService")
public class DeviceSwitchServiceImpl implements DeviceSwitchService {

	private final static Logger log = LoggerFactory.getLogger(DeviceSwitchServiceImpl.class);

	@Autowired
	private DeviceSwitchDao deviceSwitchDao;
	@Autowired
	private OmsDeviceSwitchDao omsDeviceSwitchDao;

	@Autowired
	private DeviceLogDao deviceLogDao;

	private static final String ERROR_CODE = "021";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<SwitchDto> page = new Page<SwitchDto>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsDeviceSwitchDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<SwitchDto> list = this.omsDeviceSwitchDao.findItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "????????????");
		}
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		String type = StringUtil.toStr(params.get("type"));
		Integer value = NumberUtil.toInt(params.get("value"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		log.info("??????????????????>>>>>>>>>>" + deviceId + ">>>>>>>>>>>" + type + ">>>>>>>>>>>" + value);

		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);

		switch (type) {
			case "software":
				deviceSwitch.setSoftware(value);
				break;
			case "pay":
				deviceSwitch.setPay(value);
				break;
			case "statistics":
				deviceSwitch.setStatistics(value);
				break;
			case "onlinecheck":
				deviceSwitch.setOnlinecheck(value);
				break;
			case "filecheck":
				deviceSwitch.setFilecheck(value);
				break;
			case "video":
				deviceSwitch.setVideo(value);
				break;
			case "clip":
				deviceSwitch.setClip(value);
				break;
			default:
				break;
		}
		this.deviceSwitchDao.updateByPrimaryKey(deviceSwitch);
		JSONObject logoj = new JSONObject();
		logoj.put("type", type);
		logoj.put("value", value);

		DeviceLog devicelog = LogUtil.buildLog(deviceId,6, "??????????????????", JSONObject.toJSONString(logoj), now, createdBy);
		this.deviceLogDao.insert(devicelog);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean getById(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "????????????");
		}
		String deviceId = StringUtil.toStr(params.get("deviceId"));
		DeviceSwitch deviceSwitch = this.deviceSwitchDao.selectByPrimaryKey(deviceId);
		return ResResultBean.success(deviceSwitch);
	}

}
