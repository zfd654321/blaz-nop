package com.bl.nop.dao.wx;

import com.bl.nop.entity.wx.EasyarConfig;

public interface EasyarConfigDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EasyarConfig record);

    int insertSelective(EasyarConfig record);

    EasyarConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EasyarConfig record);

    int updateByPrimaryKey(EasyarConfig record);
}