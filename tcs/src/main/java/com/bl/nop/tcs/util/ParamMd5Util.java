package com.bl.nop.tcs.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bl.nop.common.util.Md5Util;

public class ParamMd5Util {
	
	private static Logger log = LoggerFactory.getLogger(ParamMd5Util.class);
	/**
	 * 加密验证
	 * @param deviceId   deviceId+BLAZ的md5的加密方式
	 * @param apiDeviceService
	 * @return
	 */
	public static boolean encrypt(String deviceId, String deviceIdMD5) {
	   boolean isResult=false;
	   String dbDeviceId=deviceId+"BLAZ";
	   String md5 = Md5Util.toMd5(dbDeviceId);
	   log.info("请求数据加密验证，请求加密："+deviceIdMD5+"，系统加密："+md5);
	   if(StringUtils.equals(deviceIdMD5, md5)){//说明当前的设备是公司已入库的线上的设备
		   isResult=true;
	   }
	   return isResult;
	}

	public static void main(String[] args) {
		System.out.println(Md5Util.toMd5("bltest_scw01"));
	}
}
