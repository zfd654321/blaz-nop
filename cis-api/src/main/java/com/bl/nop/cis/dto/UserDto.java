package com.bl.nop.cis.dto;

import com.bl.nop.entity.sys.SysUser;

public class UserDto extends SysUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
