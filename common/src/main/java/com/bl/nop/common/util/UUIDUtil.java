package com.bl.nop.common.util;

import java.util.UUID;

public class UUIDUtil {

	public static String uid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
