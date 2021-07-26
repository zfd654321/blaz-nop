package com.bl.nop.cis.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.bl.nop.cis.api.MenuService;
import com.bl.nop.cis.dao.PermissionDao;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.dto.MenuTreeDto;
import com.bl.nop.entity.sys.SysMenu;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

	private final static Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	@Autowired
	private PermissionDao permissionDao;

	private static final String ERROR_CODE = "10102";
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

	@Override
	public ResResultBean queryAll() {
		List<SysMenu> list = this.permissionDao.findAllMenu();
		return ResResultBean.success(list);
	}

	
}
