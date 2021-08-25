package com.bl.nop.bis.util;

import com.bl.nop.common.util.Md5Util;

import org.apache.commons.lang.StringUtils;

public class ParamMd5Util {
	
	/**
	 * 加密验证
	 * @param deviceId   deviceId+BLAZ的md5的加密方式
	 * @param deviceIdMD5
	 * @return
	 */
	public static boolean encrypt(String deviceId, String deviceIdMD5) {
	   boolean isResult=false;
	   String dbDeviceId=deviceId+"BLAZ";
	   String md5 = Md5Util.toMd5(dbDeviceId);
	   if(StringUtils.equals(deviceIdMD5, md5)){//说明当前的设备是公司已入库的线上的设备
		   isResult=true;
	   }
	   return isResult;
	}

}
