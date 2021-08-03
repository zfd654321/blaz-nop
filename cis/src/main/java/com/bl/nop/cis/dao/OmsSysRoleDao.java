package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.dto.MenuTreeDto;
import com.bl.nop.entity.sys.SysRole;

import org.apache.ibatis.annotations.Param;

public interface OmsSysRoleDao {
    List<SysRole> findRoleByPage(Map<String, Object> param);

    int findRoleCount(Map<String, Object> param);

    int updateRoleMenu(@Param("roleId")String roleId, @Param("menuIdStr")String menuIdStr);

    int cleanRoleMenu(@Param("roleId")String roleId);

    List<MenuTreeDto> findAllMenu();

    List<String> findRoleMenu(@Param("roleId")String roleId);
}
