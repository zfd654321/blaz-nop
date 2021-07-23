package com.bl.nop.dao.sys;

import com.bl.nop.entity.sys.SysRoleMenu;

public interface SysRoleMenuDao {
    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);
}