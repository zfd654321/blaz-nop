package com.bl.nop.dao.game;

import com.bl.nop.entity.game.Game;

public interface GameDao {
    int deleteByPrimaryKey(String id);

    int insert(Game record);

    int insertSelective(Game record);

    Game selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Game record);

    int updateByPrimaryKey(Game record);
}