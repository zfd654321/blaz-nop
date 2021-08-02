package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.cis.dto.UserDto;
import com.bl.nop.entity.sys.SysRole;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OmsSysUserDao {
    List<UserDto> findUserByPage(Map<String, Object> params);

    int findUserCount(Map<String, Object> param);

    int updateUserStatus(@Param("userId")String userId, @Param("status")String status);

    List<SysRole> loadRoleList(Map<String, Object> param);
}
