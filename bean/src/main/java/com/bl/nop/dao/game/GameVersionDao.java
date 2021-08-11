package com.bl.nop.dao.game;

import com.bl.nop.entity.game.GameVersion;

public interface GameVersionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GameVersion record);

    int insertSelective(GameVersion record);

    GameVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameVersion record);

    int updateByPrimaryKey(GameVersion record);
}