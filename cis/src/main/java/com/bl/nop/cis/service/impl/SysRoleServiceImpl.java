package com.bl.nop.cis.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.SysRoleService;
import com.bl.nop.cis.dao.OmsSysRoleDao;
import com.bl.nop.cis.dto.MenuTreeDto;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.sys.SysRoleDao;
import com.bl.nop.entity.sys.SysRole;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	private final static Logger log = LoggerFactory.getLogger(SysRoleServiceImpl.class);

	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private OmsSysRoleDao omsSysRoleDao;

	private static final String ERROR_CODE = "010";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsSysRoleDao.findRoleCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<SysRole> list = this.omsSysRoleDao.findRoleByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "04001", "参数为空");
		}
		String id = StringUtil.toStr(params.get("id"));
		boolean edit = StringUtil.toStr(params.get("edit")).equals("true");
		String name = StringUtil.toStr(params.get("name"));
		String remarks = StringUtil.toStr(params.get("remarks"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		String menuIdStr=StringUtil.toStr(params.get("menuIdStr"));
		Date now = new Date();

		if (edit) {// 修改
			SysRole role = this.sysRoleDao.selectByPrimaryKey(id);
			role.setName(name);
			role.setRemarks(remarks);
			role.setUpdatedAt(now);
			role.setUpdatedBy(createdBy);
			this.sysRoleDao.updateByPrimaryKey(role);
			this.omsSysRoleDao.cleanRoleMenu(id);
			this.omsSysRoleDao.updateRoleMenu(id, menuIdStr);

		} else {// 新增
			SysRole role = new SysRole();
			role.setId(id);
			role.setName(name);
			role.setRemarks(remarks);
			role.setCreatedAt(now);
			role.setCreatedBy(createdBy);
			role.setUpdatedAt(now);
			role.setUpdatedBy(createdBy);
			this.sysRoleDao.insert(role);
			this.omsSysRoleDao.updateRoleMenu(id, menuIdStr);
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadMenu() {
		List<MenuTreeDto> menuList = this.omsSysRoleDao.findAllMenu();
		List<MenuTreeDto> showMenus = new ArrayList<MenuTreeDto>();
		if(null != menuList && !menuList.isEmpty()) {
			List<MenuTreeDto> childMenus = null;
			for (MenuTreeDto menu : menuList) {
				childMenus = new ArrayList<MenuTreeDto>();
				for (MenuTreeDto childmenu : menuList) {
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
	public ResResultBean loadRoleMenu(String roleId) {
		List<String> roleMenuList = this.omsSysRoleDao.findRoleMenu(roleId);
		return ResResultBean.success(roleMenuList);
	}

	@Override
	public ResResultBean getByUserId(String userId) {
		log.info("获取用户信息>>>>>userId:" + userId);
		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "02001", "用户id为空");
		}

		SysRole user = this.sysRoleDao.selectByPrimaryKey(userId);
		if (null == user) {
			return ResResultBean.error(ERROR_CODE + "02002", "用户不存在");
		}

		return ResResultBean.success(user);
	}

}
