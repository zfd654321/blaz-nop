package com.bl.nop.cis.dao;

import java.util.List;

import com.bl.nop.dto.MenuTreeDto;
import com.bl.nop.entity.sys.SysUser;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao {

	SysUser getUserByUserNoAndPass(@Param("username") String username, @Param("password") String password);

	List<MenuTreeDto> findMenuByRoleId(String roleId);

}
