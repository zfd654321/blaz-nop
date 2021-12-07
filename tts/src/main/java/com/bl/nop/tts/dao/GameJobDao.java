package com.bl.nop.tts.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.tts.dto.DataGameDto;

import org.springframework.stereotype.Repository;

@Repository
public interface GameJobDao {

    List<DataGameDto> queryGameDayData(Map<String, Object> params);

    int cleanGameDayData(Map<String, Object> params);

}
