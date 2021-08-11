package com.bl.nop.dao.resources;

import com.bl.nop.entity.resources.Resources;

public interface ResourcesDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Resources record);

    int insertSelective(Resources record);

    Resources selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Resources record);

    int updateByPrimaryKey(Resources record);
}