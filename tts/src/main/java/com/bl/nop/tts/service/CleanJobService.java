package com.bl.nop.tts.service;

import java.util.Date;

import com.bl.nop.common.bean.ResResultBean;

public interface CleanJobService {
    /**
     * 31天前的数据和临时文件清理
     * @param params
     * @return 
     */
	public ResResultBean DayCleanJob(Date cleanDate);
}
