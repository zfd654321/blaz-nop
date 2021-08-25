package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface DeviceService {

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

	/**
	 * 空闲pc列表
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean freePcList(Map<String, Object> params);

	/**
	 * 设备入库
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean intoHouse(Map<String, Object> params);

	/**
	 * 设备上下线
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean updateStatus(Map<String, Object> params);

	/**
	 * 设备删除
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean delete(Map<String, Object> params);

	/**
	 * 读取设备配置
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean loadDeviceConfig(Map<String, Object> params);

	/**
	 * 保存设备配置
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean saveDeviceConfig(Map<String, Object> params);

	/**
	 * 读取设备广告
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean loadDeviceAdvert(Map<String, Object> params);

	/**
	 * 保存设备广告
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean saveDeviceAdvert(Map<String, Object> params);

	/**
	 * 读取设备游戏
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean loadDeviceGame(Map<String, Object> params);

	/**
	 * 读取设备游戏2d资源
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean loadDeviceGameNetres(Map<String, Object> params);

	/**
	 * 保存设备游戏
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean saveDeviceGame(Map<String, Object> params);


	/**
	 * 设备配置平移
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean deviceCopy(Map<String, Object> params);

	/**
	 * 设备上线校验
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean deviceOnlineCheck(Map<String, Object> params);

}
