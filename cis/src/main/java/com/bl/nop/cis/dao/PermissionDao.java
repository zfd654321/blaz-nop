package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.cis.dto.UserDto;
import com.bl.nop.dto.MenuTreeDto;
import com.bl.nop.entity.sys.SysMenu;
import com.bl.nop.entity.sys.SysRole;
import com.bl.nop.entity.sys.SysUser;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao {

	SysUser getUserByUserNoAndPass(@Param("username")String username, @Param("password")String password);
	
	List<UserDto> findUserByPage(Map<String, Object> params);
	
	int findUserCount(Map<String, Object> param);
    
    int updateUserStatus(@Param("userId")String userId, @Param("status")String status);
    
    List<MenuTreeDto> findMenuByRoleId(String roleId);

    List<SysMenu> findMenuByMenuIds(List<String> list);
    
    List<SysMenu> findAllMenu();

	List<SysRole> findRoleByPage(Map<String, Object> param);

	int findRoleCount(Map<String, Object> param);

	int updateRoleMenu(@Param("roleId")String roleId, @Param("menuIdStr")String menuIdStr);
	
	int cleanRoleMenu(@Param("roleId")String roleId);

	List<UserDto> findDeviceUser();

	void cleanUserMenu(Map<String, Object> params);

	void saveUserMenu(Map<String, Object> params);

}
