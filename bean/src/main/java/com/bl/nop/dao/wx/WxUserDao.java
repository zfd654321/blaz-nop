package com.bl.nop.dao.wx;

import com.bl.nop.entity.wx.WxUser;

public interface WxUserDao {
    int deleteByPrimaryKey(String openId);

    int insert(WxUser record);

    int insertSelective(WxUser record);

    WxUser selectByPrimaryKey(String openId);

    int updateByPrimaryKeySelective(WxUser record);

    int updateByPrimaryKey(WxUser record);
}