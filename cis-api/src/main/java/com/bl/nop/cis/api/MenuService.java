package com.bl.nop.cis.api;


import com.bl.nop.common.bean.ResResultBean;

public interface MenuService {
	/**
	 * 根据角色ID获取该角色的菜单
	 * @param roleId
	 * @return
	 */
	public ResResultBean getMenuTree(String roleId);
	/**
	 * 获取所有菜单
	 * @return
	 */
	public ResResultBean queryAll();
}
