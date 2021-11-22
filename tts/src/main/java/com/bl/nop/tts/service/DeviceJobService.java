package com.bl.nop.tts.service;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;


public interface DeviceJobService {
    /**
     * 每小时设备数据统计
     * @param params
     * @return
     */
	public ResResultBean deviceHourStatic(Map<String, Object> params);
    /**
     * 每天设备数据统计
     * @param params
     * @return
     */
	public ResResultBean deviceDayStatic(Map<String, Object> params);
    /**
     * 每月设备数据统计
     * @param params
     * @return
     */
	public ResResultBean deviceMonthStatic(Map<String, Object> params);
}
