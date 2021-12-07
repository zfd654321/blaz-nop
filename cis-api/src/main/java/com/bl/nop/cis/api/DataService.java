package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface DataService {

	/**
	 * 分页查询信息
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean queryByPage(Map<String, Object> param);

	/**
	 * 可视化信息查询
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean showData(Map<String, Object> params);

}
