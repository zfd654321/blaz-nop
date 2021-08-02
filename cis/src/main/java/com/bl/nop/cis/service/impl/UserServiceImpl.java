package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.UserService;
import com.bl.nop.cis.dao.OmsSysUserDao;
import com.bl.nop.cis.dto.UserDto;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.sys.SysUserDao;
import com.bl.nop.entity.sys.SysRole;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.util.CommonConst;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

	private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private SysUserDao SysUserDao;
	@Autowired
	private OmsSysUserDao userDao;

	private static final String ERROR_CODE = "10101";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<UserDto> page = new Page<UserDto>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.userDao.findUserCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<UserDto> list = this.userDao.findUserByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "04001", "参数为空");
		}

		String userId = StringUtil.toStr(params.get("userId"));
		String username = StringUtil.toStr(params.get("username"));
		String nickname = StringUtil.toStr(params.get("nickname"));
		String role = StringUtil.toStr(params.get("role"));
		String pass = StringUtil.toStr(params.get("pass"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		SysUser user = new SysUser();
		Date now = new Date();
		user.setId(userId);
		user.setNickname(nickname);
		user.setUsername(username);
		user.setPassword(Md5Util.toMd5(pass));
		user.setRole(role);
		user.setStatus(1);
		user.setCreatedBy(createdBy);
		user.setCreatedAt(now);
		user.setUpdatedBy(createdBy);
		user.setUpdatedAt(now);
		this.SysUserDao.insert(user);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadRole(Map<String, Object> params) {
		List<SysRole> list = this.userDao.loadRoleList(params);
		return ResResultBean.success(list);
	}



	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String userId = StringUtil.toStr(params.get("userId"));
		log.info("删除用户信息>>>>>userId:" + userId);
		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "01001", "用户id为空");
		}
		int num = this.userDao.updateUserStatus(userId, CommonConst.NO);
		if (num < 1) {
			return ResResultBean.error(ERROR_CODE + "01002", "删除用户失败");
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean getByUserId(String userId) {
		log.info("获取用户信息>>>>>userId:" + userId);
		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "02001", "用户id为空");
		}

		SysUser user = this.SysUserDao.selectByPrimaryKey(userId);
		if (null == user) {
			return ResResultBean.error(ERROR_CODE + "02002", "用户不存在");
		}

		return ResResultBean.success(user);
	}

}
