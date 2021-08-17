package com.bl.nop.dao.version;

import com.bl.nop.entity.version.Version;

public interface VersionDao {
    int deleteByPrimaryKey(String id);

    int insert(Version record);

    int insertSelective(Version record);

    Version selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Version record);

    int updateByPrimaryKey(Version record);
}