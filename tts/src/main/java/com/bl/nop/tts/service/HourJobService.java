package com.bl.nop.tts.service;

import com.bl.nop.common.bean.ResResultBean;


public interface HourJobService {
    /**
     * 每小时设备数据统计
     * @return
     */
	public ResResultBean deviceHourStatic();
}
