package com.bl.nop.dao.sys;

import com.bl.nop.entity.sys.SysRole;

public interface SysRoleDao {
    int insert(SysRole record);

    int insertSelective(SysRole record);
}