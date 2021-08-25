package com.bl.nop.dao.wx;

import com.bl.nop.entity.wx.PhotoGroup;

public interface PhotoGroupDao {
    int deleteByPrimaryKey(String id);

    int insert(PhotoGroup record);

    int insertSelective(PhotoGroup record);

    PhotoGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PhotoGroup record);

    int updateByPrimaryKey(PhotoGroup record);
}