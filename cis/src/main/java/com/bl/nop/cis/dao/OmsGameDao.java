package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.game.Game;

public interface OmsGameDao {
    List<Game> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);
}
