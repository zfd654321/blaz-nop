package com.bl.nop.tts.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.data.DataGame;

import org.springframework.stereotype.Repository;

@Repository
public interface GameJobDao {

    List<DataGame> queryGameDayData(Map<String, Object> params);

    int cleanGameDayData(Map<String, Object> params);

}
