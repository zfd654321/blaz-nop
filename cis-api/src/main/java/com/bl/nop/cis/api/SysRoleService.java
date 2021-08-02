package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface SysRoleService {

	/**
	 * 分页查询用户信息
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean queryByPage(Map<String, Object> param);
	/**
	 * 保存用户信息
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean save(Map<String, Object> params);

	/**
	 * 加载角色列表
	 * 
	 * @param user
	 * @return
	 */
	public ResResultBean loadRole(Map<String, Object> params);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	public ResResultBean delete(Map<String, Object> param);

	/**
	 * 根据id获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public ResResultBean getByUserId(String userId);

}
