package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.game.Game;
import com.bl.nop.entity.game.GameVersion;

public interface OmsGameDao {
    List<Game> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);

    void cleanGameBetres(String id);

    GameVersion getGameVersion(Map<String, Object> params);

    List<GameVersion> queryOutGameVersion(String id);

    List<GameVersion> queryGameVersionList(String id);
}
