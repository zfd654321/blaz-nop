package com.bl.nop.tts.service;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface GameJobService {
    /**
     * 每天游戏数据统计
     * @param params
     * @return
     */
	public ResResultBean gameDayStatic(Map<String, Object> params);
}
