package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface SysRoleService {

	/**
	 * 分页查询角色信息
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean queryByPage(Map<String, Object> param);

	/**
	 * 保存角色信息
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean save(Map<String, Object> params);

	/**
	 * 加载菜单列表
	 * 
	 * @param user
	 * @return
	 */
	public ResResultBean loadMenu();

	/**
	 * 加载菜单列表
	 * 
	 * @param user
	 * @return
	 */
	public ResResultBean loadRoleMenu(String roleId);

	/**
	 * 根据id获取角色信息
	 * 
	 * @param userId
	 * @return
	 */
	public ResResultBean getByUserId(String userId);

}
