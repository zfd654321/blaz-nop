package com.bl.nop.dao.wx;

import com.bl.nop.entity.wx.PhotoImg;

public interface PhotoImgDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PhotoImg record);

    int insertSelective(PhotoImg record);

    PhotoImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PhotoImg record);

    int updateByPrimaryKey(PhotoImg record);
}