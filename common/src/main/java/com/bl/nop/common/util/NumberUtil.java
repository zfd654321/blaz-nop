package com.bl.nop.common.util;

public class NumberUtil {

	/**
	 * 字符串转化成整型，字符串为空则返回默认值
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static int toInt(String obj, int defaultValue) {
		if(null == obj || "".equals(obj)) {
			return defaultValue;
		}
		
		try {
			int i = Integer.parseInt(obj);
			return i;
		} catch (Exception e) {
		}
		return defaultValue;
	}
	/**
	 * 实体类转化成整型，实体类为空则返回默认值
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static int toInt(Object obj, int defaultValue) {
		if(null == obj) {
			return defaultValue;
		}
		return toInt(obj.toString(), defaultValue);
	}
	/**
	 * 实体类转化成整型
	 * @param obj
	 * @return
	 */
	public static Integer toInt(Object obj) {
		if(null == obj) {
			return null;
		}
		
		try {
			int i = Integer.parseInt(obj.toString());
			return i;
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 字符串转化成整型
	 * @param obj
	 * @return
	 */
	public static Integer toInt(String obj) {
		if(null == obj || "".equals(obj)) {
			return null;
		}
		
		try {
			int i = Integer.parseInt(obj);
			return i;
		} catch (Exception e) {
		}
		return null;
	}
	
	
}
