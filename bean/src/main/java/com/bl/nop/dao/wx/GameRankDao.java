package com.bl.nop.dao.wx;

import com.bl.nop.entity.wx.GameRank;

public interface GameRankDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GameRank record);

    int insertSelective(GameRank record);

    GameRank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameRank record);

    int updateByPrimaryKey(GameRank record);
}