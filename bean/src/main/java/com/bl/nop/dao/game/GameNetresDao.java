package com.bl.nop.dao.game;

import com.bl.nop.entity.game.GameNetres;

public interface GameNetresDao {
    int insert(GameNetres record);

    int insertSelective(GameNetres record);
}