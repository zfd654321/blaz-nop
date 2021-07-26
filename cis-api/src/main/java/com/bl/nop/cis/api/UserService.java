package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface UserService {
	/**
	 * 登录
	 * 
	 * @param tentinetMail
	 * @return
	 */
	public ResResultBean loginUser(String userNo, String userPass);

	/**
	 * 保存用户信息
	 * 
	 * @param params
	 * @return
	 */
	public ResResultBean save(Map<String, Object> params);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	public ResResultBean update(Map<String, Object> params);

	/**
	 * 分页查询用户信息
	 * 
	 * @param param
	 * @return
	 */
	public ResResultBean queryByPage(Map<String, Object> param);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	public ResResultBean delete(Map<String, Object> param);

	/**
	 * 根据USERID获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public ResResultBean getByUserId(String userId);

	/**
	 * 修改用户密码
	 * 
	 * @param newPass
	 * @param userId
	 * @param string 
	 * @return
	 */
	public ResResultBean updatePass(String oldPass,String newPass, String userId);

	/**
	 * 获取用户列表
	 * @return
	 */
	public ResResultBean loadUserList();

	/**
	 * 更新密码
	 * @param oldPass
	 * @param userPass
	 * @return
	 */
	public ResResultBean verifyOldPass(String oldPass, String userPass);

}
