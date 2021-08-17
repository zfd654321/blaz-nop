package com.bl.nop.tcs.service.device.impl;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DeviceService;
import com.bl.nop.common.exception.BusinessException;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.entity.device.Device;
import com.bl.nop.tcs.service.device.ApiService;
import com.bl.nop.tcs.util.ParamMd5Util;
import com.bl.nop.tcs.util.SpringContextUtil;

@Service("apiService")
public class ApiServiceImpl implements ApiService {

	private static Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
	
	private final static String ERROR_CODE = "11";
	@Autowired
	private DeviceService deviceService;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object handle(Map<String, Object> params) {
		if(null == params || params.isEmpty()) {
			throw new BusinessException(ERROR_CODE + "011", "参数为空");
		}
		
		String sId = StringUtil.toStr(params.get("s_id"));
		if(StringUtils.isBlank(sId)) {
			throw new BusinessException(ERROR_CODE + "012", "s_id为空");
		}
		
		String method = StringUtil.toStr(params.get("method"));
		if(StringUtils.isBlank(method)) {
			throw new BusinessException(ERROR_CODE + "013", "未找到访问处理类");
		}
		
		String[] methods = method.split("_");
		if(null == methods || methods.length < 2) {
			throw new BusinessException(ERROR_CODE + "014", "未找到访问处理方法");
		}
		
		String claz = methods[0];
		claz = setDefaultClass(claz);
		String severMethodName = methods[1];
		log.info("请求处理类："+claz+"，处理方法："+severMethodName);
		
		String deviceIdMd5 = StringUtil.toStr(params.get("device_idMD5"));
		if(StringUtils.isEmpty(deviceIdMd5)) {
			log.info("----------s_id["+sId+"]未对参数进行加密----------");
			throw new BusinessException(ERROR_CODE + "015", "设备["+sId+"]未对参数进行加密");
		}
		
		Device Device = deviceService.getOnlineDeviceByPcId(sId);
		if(null == Device) {
			log.info("----------s_id["+sId+"]未找到设备----------");
			throw new BusinessException(ERROR_CODE + "016", "设备["+sId+"]没有在BL控制中心上线");
		}
		
		String deviceId = Device.getDeviceId();
		boolean isEncrypt = ParamMd5Util.encrypt(deviceId, deviceIdMd5);
		if(!isEncrypt) {
			log.info("-------------设备["+deviceId+"]验证参数加密不对，加密数："+deviceIdMd5);
			throw new BusinessException(ERROR_CODE + "017", "参数验证加密不合法");
		}
		
		Method method2 = null;
		Object object = null;
		Class clazz = null;
		try {
			object = SpringContextUtil.getBean(claz);
			log.info("获取运行类为："+object);
			clazz = SpringContextUtil.getType(claz);
			log.info("获取运行类Class为："+clazz);
			method2 = clazz.getMethod(severMethodName, Map.class);
		} catch (Exception e) {
			log.error("未找到执行类", e);
			throw new BusinessException(ERROR_CODE + "018", "未找到执行类");
		} 
		
		Object retObj = new JSONObject();
		try {
			params.put("device_id", deviceId);
			log.info("获取运行类["+clazz+"]，运行方法"+method2+"，参数："+params);
			retObj = method2.invoke(object, params);
			log.info("获取运行类["+clazz+"]，运行方法"+method2+"，参数["+params+"]运行结束，返回数据："+retObj);
		} catch (Exception e) {
			log.error("执行方法失败", e);
			throw new BusinessException(ERROR_CODE + "019", "操作失败");
		} 
		
		return retObj;
	}
	/**
	 * 设置默认类，当方法为getDeviceFileContent获取设备全局文件
	 * @param serviceMethodName
	 * @return
	 */
	private String setDefaultClass(String serviceMethodName) {
		return StringUtils.equals("getDeviceFileContent", serviceMethodName)?"fileReaderService":serviceMethodName;
	}

}
