package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bl.nop.cis.api.DeviceService;
import com.bl.nop.cis.dao.OmsDeviceDao;
import com.bl.nop.cis.util.PropertyUtil;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.device.DeviceConfigDao;
import com.bl.nop.dao.device.DeviceDao;
import com.bl.nop.dao.device.DevicePcDao;
import com.bl.nop.dao.device.DeviceSwitchDao;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DeviceConfig;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.device.DeviceSwitch;

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
				repc.setStatus(1);
				pc.setStatus(2);
				this.devicePcDao.updateByPrimaryKey(repc);
				this.devicePcDao.updateByPrimaryKey(pc);
				item.setPcId(pcId);
			}
			item.setName(name);
			item.setRemarks(remarks);
			item.setAddress(address);
			item.setType(type);
			item.setScreen(screen);
			item.setCamera(camera);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.deviceDao.updateByPrimaryKey(item);

		} else {// 新增
			log.info("新增设备>>>>>deviceId:" + deviceId);
			if (reitem != null) {
				return ResResultBean.error(ERROR_CODE + "002", "该设备码已存在，请勿重复添加");
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
			item.setCreatedAt(now);
			item.setCreatedBy(createdBy);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.deviceDao.insert(item);
			pc.setStatus(2);
			this.devicePcDao.updateByPrimaryKey(pc);

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
			this.deviceSwitchDao.insert(deviceSwitch);
		}

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
		Device device = this.deviceDao.selectByPrimaryKey(deviceId);
		if (device != null) {
			DevicePc pc = this.devicePcDao.selectByPrimaryKey(device.getPcId());
			if (pc != null && pc.getStatus() == 2) {
				pc.setStatus(1);
				this.devicePcDao.updateByPrimaryKey(pc);
			}
			this.deviceDao.deleteByPrimaryKey(deviceId);
		}

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
		DeviceConfig deviceConfig = JSON.parseObject(JSON.toJSONString(params), DeviceConfig.class);
		this.deviceConfigDao.updateByPrimaryKey(deviceConfig);
		return ResResultBean.success();
	}

}
