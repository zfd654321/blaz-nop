package com.bl.nop.dao.wx;

import com.bl.nop.entity.wx.WxRank;

public interface WxRankDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WxRank record);

    int insertSelective(WxRank record);

    WxRank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxRank record);

    int updateByPrimaryKey(WxRank record);
}