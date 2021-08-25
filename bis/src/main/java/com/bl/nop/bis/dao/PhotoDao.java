package com.bl.nop.bis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.bis.dto.GameRankDto;

import org.springframework.stereotype.Repository;

@Repository
public interface PhotoDao {

    List<GameRankDto> qeuryTopList(Map<String, Object> qparams);

    Integer queryGameRank(Map<String, Object> qparams);


}
