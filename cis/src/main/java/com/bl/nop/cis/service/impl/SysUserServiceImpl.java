package com.bl.nop.cis.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.SysUserService;
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

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	private final static Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private OmsSysUserDao omsSysUserDao;

	private static final String ERROR_CODE = "011";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<UserDto> page = new Page<UserDto>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsSysUserDao.findUserCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<UserDto> list = this.omsSysUserDao.findUserByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "04001", "????????????");
		}
		String id=StringUtil.toStr(params.get("id"));
		boolean edit=StringUtil.toStr(params.get("edit")).equals("true");
		String username = StringUtil.toStr(params.get("username"));
		String password = StringUtil.toStr(params.get("password"));
		String nickname = StringUtil.toStr(params.get("nickname"));
		String role = StringUtil.toStr(params.get("role"));
		Integer status=NumberUtil.toInt(params.get("status"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		
		if(edit){//??????
			SysUser user=this.sysUserDao.selectByPrimaryKey(id);
			user.setUsername(username);
			if(StringUtils.isNotBlank(password)){
				user.setPassword(Md5Util.toMd5(password));
			}
			user.setNickname(nickname);
			user.setRole(role);
			user.setStatus(status);
			user.setUpdatedAt(now);
			user.setUpdatedBy(createdBy);
			this.sysUserDao.updateByPrimaryKey(user);
		}else{//??????
			SysUser user = new SysUser();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String newUserId = "BL" + format.format(new Date());
			user.setId(newUserId);
			user.setUsername(username);
			user.setPassword(Md5Util.toMd5(password));
			user.setNickname(nickname);
			user.setRole(role);
			user.setStatus(status);
			user.setCreatedAt(now);
			user.setCreatedBy(createdBy);
			user.setUpdatedAt(now);
			user.setUpdatedBy(createdBy);
			this.sysUserDao.insert(user);
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadRole(Map<String, Object> params) {
		List<SysRole> list = this.omsSysUserDao.loadRoleList(params);
		return ResResultBean.success(list);
	}



	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String userId = StringUtil.toStr(params.get("userId"));
		log.info("??????????????????>>>>>userId:" + userId);
		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "01001", "??????id??????");
		}
		int num = this.omsSysUserDao.updateUserStatus(userId, CommonConst.NO);
		if (num < 1) {
			return ResResultBean.error(ERROR_CODE + "01002", "??????????????????");
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean getByUserId(String userId) {
		log.info("??????????????????>>>>>userId:" + userId);
		if (StringUtils.isBlank(userId)) {
			return ResResultBean.error(ERROR_CODE + "02001", "??????id??????");
		}

		SysUser user = this.sysUserDao.selectByPrimaryKey(userId);
		if (null == user) {
			return ResResultBean.error(ERROR_CODE + "02002", "???????????????");
		}

		return ResResultBean.success(user);
	}

}
