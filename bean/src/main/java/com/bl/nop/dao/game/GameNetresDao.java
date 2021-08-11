package com.bl.nop.dao.game;

import com.bl.nop.entity.game.GameNetres;

public interface GameNetresDao {
    int deleteByPrimaryKey(String id);

    int insert(GameNetres record);

    int insertSelective(GameNetres record);

    GameNetres selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GameNetres record);

    int updateByPrimaryKey(GameNetres record);
}