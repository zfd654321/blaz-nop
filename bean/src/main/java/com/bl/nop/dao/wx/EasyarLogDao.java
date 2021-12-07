package com.bl.nop.dao.wx;

import com.bl.nop.entity.wx.EasyarLog;

public interface EasyarLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EasyarLog record);

    int insertSelective(EasyarLog record);

    EasyarLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EasyarLog record);

    int updateByPrimaryKey(EasyarLog record);
}