package com.bl.nop.common.util;

public class StringUtil {

	public static String toStr(Object obj) {
		return obj!=null?obj.toString():null;
	}
	
	public static String toBlank(String str) {
		return str!=null?str:"";
	}
}
