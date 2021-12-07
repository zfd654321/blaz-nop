package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface DeviceSwitchService {

	/**
	 * 分页查询信息
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean queryByPage(Map<String, Object> param);

	/**
	 * 保存信息
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean save(Map<String, Object> params);

    public ResResultBean getById(Map<String, Object> params);

}
