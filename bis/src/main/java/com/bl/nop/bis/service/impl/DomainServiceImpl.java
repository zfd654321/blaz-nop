package com.bl.nop.bis.service.impl;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DomainService;
import com.bl.nop.bis.dao.ApiDao;
import com.bl.nop.bis.util.ParamMd5Util;
import com.bl.nop.common.exception.BusinessException;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.entity.device.Device;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("domainService")
public class DomainServiceImpl implements DomainService {

	private static Logger log = LoggerFactory.getLogger(DomainServiceImpl.class);
	
	private final static String ERROR_CODE = "11";
	@Autowired
	private ApiDao apiDao;
	@Override
	public JSONObject handle(Map<String, Object> params) {
		if(null == params || params.isEmpty()) {
			throw new BusinessException(ERROR_CODE + "011", "参数为空");
		}
		
		String pcId = StringUtil.toStr(params.get("pcId"));
		if(StringUtils.isBlank(pcId)) {
			throw new BusinessException(ERROR_CODE + "012", "pcId为空");
		}
		
		String deviceIdMd5 = StringUtil.toStr(params.get("deviceIdMd5"));
		if(StringUtils.isEmpty(deviceIdMd5)) {
			log.info("----------pcId["+pcId+"]未对参数进行加密----------");
			throw new BusinessException(ERROR_CODE + "015", "设备["+pcId+"]未对参数进行加密");
		}
		
		Device Device = apiDao.getOnlineDeviceByPcId(pcId);
		if(null == Device) {
			log.info("----------pcId["+pcId+"]未找到设备----------");
			throw new BusinessException(ERROR_CODE + "016", "设备["+pcId+"]没有在后台控制中心上线");
		}
		
		String deviceId = Device.getDeviceId();
		boolean isEncrypt = ParamMd5Util.encrypt(deviceId, deviceIdMd5);
		if(!isEncrypt) {
			log.info("-------------设备["+deviceId+"]验证参数加密不对，加密数："+deviceIdMd5);
			throw new BusinessException(ERROR_CODE + "017", "参数验证加密不合法");
		}
		
		JSONObject retObj = new JSONObject();
		retObj.put("deviceId", deviceId);
		
		return retObj;
	}
}
