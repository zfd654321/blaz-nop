package com.bl.nop.cis.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.bl.nop.cis.api.PermissionService;
import com.bl.nop.cis.dao.PermissionDao;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.dao.sys.SysUserDao;
import com.bl.nop.dto.MenuTreeDto;
import com.bl.nop.entity.sys.SysUser;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

	private final static Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private SysUserDao SysUserDao;

	private static final String ERROR_CODE = "000";

	/* 用户登陆 */
	@Override
	public ResResultBean loginUser(String username, String password) {
		log.info("用户登陆>>>>账号：" + username + ">>>>>密码：" + password);
		String md5Pass = Md5Util.toMd5(password);
		SysUser user = this.permissionDao.getUserByUserNoAndPass(username, md5Pass);
		if (user != null) {
			return ResResultBean.success(user);
		} else {
			return ResResultBean.error(ERROR_CODE + "01010", "用户名或密码错误");
		}
	}

	/* 获取角色菜单 */
	@Override
	public ResResultBean getMenuTree(String roleId) {
		if(StringUtils.isEmpty(roleId)) {
			log.info("获取菜单>>>>>角色为空");
			return ResResultBean.error(ERROR_CODE+"01001", "角色id为空");
		}
		
		List<MenuTreeDto> showMenus = new ArrayList<MenuTreeDto>();
		List<MenuTreeDto> listMenus = permissionDao.findMenuByRoleId(roleId);
		if(null != listMenus && !listMenus.isEmpty()) {
			List<MenuTreeDto> childMenus = null;
			for (MenuTreeDto menu : listMenus) {
				childMenus = new ArrayList<MenuTreeDto>();
				for (MenuTreeDto childmenu : listMenus) {
					if (menu.getId().equals(childmenu.getParMenuId())) {
						childMenus.add(childmenu);
					}
				}
				menu.setChildren(childMenus);
				if ("-1".equals(menu.getParMenuId())) {
					showMenus.add(menu);
				}
			}
		}
		
		return ResResultBean.success(showMenus);
	}


	/* 更新用户密码 */
	@Override
	public ResResultBean updatePass(String oldPass, String newPass, String userId) {
		if (StringUtils.isBlank(newPass)) {
			return ResResultBean.error(ERROR_CODE + "03001", "新密码为空");
		}

		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "03002", "用户id为空");
		}

		SysUser user = this.SysUserDao.selectByPrimaryKey(userId);
		if (null == user) {
			return ResResultBean.error(ERROR_CODE + "03003", "用户不存在");
		}
		String oldPassMd5 = Md5Util.toMd5(oldPass);
		if (!StringUtils.equals(oldPassMd5, user.getPassword())) {
			return ResResultBean.error(ERROR_CODE + "03003", "旧密码不正确");
		}

		log.info("用户[" + user.getUsername() + "]修改密码，新密码为：" + newPass);
		String md5Pass = Md5Util.toMd5(newPass);
		user.setPassword(md5Pass);

		this.SysUserDao.updateByPrimaryKey(user);
		return ResResultBean.success();
	}
	
}
