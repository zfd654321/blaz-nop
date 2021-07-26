package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.UserService;
import com.bl.nop.cis.dao.PermissionDao;
import com.bl.nop.cis.dto.UserDto;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.sys.SysUserDao;
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
	private SysUserDao userDao;
	@Autowired
	private PermissionDao permissionDao;

	private static final String ERROR_CODE = "10101";

	@Override
	public ResResultBean loginUser(String userNo, String userPass) {
		log.info("用户登陆>>>>账号：" + userNo + ">>>>>密码：" + userPass);
		String md5Pass = Md5Util.toMd5(userPass);
		SysUser user = this.permissionDao.getUserByUserNoAndPass(userNo, md5Pass);
		if (user != null) {
			return ResResultBean.success(user);
		} else {
			return ResResultBean.error(ERROR_CODE + "01010", "用户名或密码错误");
		}
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
		this.userDao.insert(user);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean update(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "05001", "参数为空");
		}

		log.info("修改用户，参数：" + params);
		String userId = StringUtil.toStr(params.get("userId"));
		SysUser user = this.userDao.selectByPrimaryKey(userId);
		if (null == user) {
			return ResResultBean.error(ERROR_CODE + "05002", "用户不存在");
		}

		String username = StringUtil.toStr(params.get("username"));
		String nickname = StringUtil.toStr(params.get("nickname"));
		String role = StringUtil.toStr(params.get("role"));
		String pass = StringUtil.toStr(params.get("pass"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		String status = StringUtil.toStr(params.get("status"));

		Date now = new Date();
		user.setNickname(nickname);
		user.setUsername(username);
		user.setPassword(Md5Util.toMd5(pass));
		user.setRole(role);
		user.setStatus(Integer.valueOf(status));
		user.setUpdatedBy(createdBy);
		user.setUpdatedAt(now);

		this.userDao.updateByPrimaryKey(user);

		return ResResultBean.success();
	}

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int currentPage = NumberUtil.toInt(param.get("currentPage"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<UserDto> page = new Page<UserDto>(currentPage, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.permissionDao.findUserCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < currentPage) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<UserDto> list = this.permissionDao.findUserByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String userId = StringUtil.toStr(params.get("userId"));
		log.info("删除用户信息>>>>>userId:" + userId);
		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "01001", "用户id为空");
		}

		int num = this.permissionDao.updateUserStatus(userId, CommonConst.NO);
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

		SysUser user = this.userDao.selectByPrimaryKey(userId);
		if (null == user) {
			return ResResultBean.error(ERROR_CODE + "02002", "用户不存在");
		}

		return ResResultBean.success(user);
	}

	@Override
	public ResResultBean updatePass(String oldPass, String newPass, String userId) {
		if (StringUtils.isBlank(newPass)) {
			return ResResultBean.error(ERROR_CODE + "03001", "新密码为空");
		}

		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "03002", "用户id为空");
		}

		SysUser user = this.userDao.selectByPrimaryKey(userId);
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

		this.userDao.updateByPrimaryKey(user);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadUserList() {
		List<UserDto> list = this.permissionDao.findDeviceUser();
		return ResResultBean.success(list);
	}

	@Override
	public ResResultBean verifyOldPass(String oldPass, String userPass) {
		String md5Pass = Md5Util.toMd5(oldPass);
		if (StringUtils.equals(md5Pass, userPass)) {
			return ResResultBean.success();
		} else {
			return ResResultBean.error();
		}
	}

}
