package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface MerchantService {

	/**
	 * 分页查询信息
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean queryByPage(Map<String, Object> param);

	/**
	 * 保存商家信息
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean save(Map<String, Object> params);

	/**
	 * 获取商家权限
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean loadPower(Map<String, Object> params);

	/**
	 * 保存设备权限
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean deviceSave(Map<String, Object> params);

	
	/**
	 * 保存游戏权限
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean gameSave(Map<String, Object> params);

	
	/**
	 * 保存广告权限
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean advertSave(Map<String, Object> params);

	/**
	 * 保存资源池大小
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean poolSave(Map<String, Object> params);

}
