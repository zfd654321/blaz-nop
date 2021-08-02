package com.bl.nop.cis.api;

import com.bl.nop.common.bean.ResResultBean;

public interface PermissionService {
	/**
	 * 登录
	 * 
	 * @param tentinetMail
	 * @return
	 */
	public ResResultBean loginUser(String username, String password);

	/**
	 * 根据角色ID获取该角色的菜单
	 * 
	 * @param roleId
	 * @return
	 */
	public ResResultBean getMenuTree(String roleId);

	/**
	 * 修改用户密码
	 * 
	 * @param newPass
	 * @param userId
	 * @param string
	 * @return
	 */
	public ResResultBean updatePass(String oldPass, String newPass, String userId);


}
