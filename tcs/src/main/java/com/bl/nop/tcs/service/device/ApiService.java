package com.bl.nop.tcs.service.device;

import java.util.Map;


public interface ApiService {
	/**
	 * 对外提供api接口处理加密，参数
	 * @param params
	 * @return
	 */
	public Object handle(Map<String, Object> params);
}
