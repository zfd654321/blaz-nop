package com.bl.nop.tts.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.dao.data.DataDeviceHourDao;
import com.bl.nop.entity.data.DataDeviceHour;
import com.bl.nop.tts.dao.HourJobDao;
import com.bl.nop.tts.service.HourJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "HourJobService")
public class HourJobServiceImpl implements HourJobService{

    @Autowired
	private HourJobDao hourJobDao;

    @Autowired
	private DataDeviceHourDao dataDeviceHourDao;
	private final static Logger log = LoggerFactory.getLogger(HourJobServiceImpl.class);

    @Override
    public ResResultBean deviceHourStatic() {
        log.info(">>>>>>>>>>>>>>>>>deviceHourStatic 执行...");
        Date now=new Date();
        Date statsTime=DateUtil.addHour(now, -1);
        Integer statsHour=DateUtil.getHourOfDay(statsTime);
        String statsDate=DateUtil.dateToStrShort(statsTime);

        Map<String,Object> params=new HashMap<>();
        params.put("statsHour", statsHour);
        params.put("statsDate", statsDate);
        List<DataDeviceHour> list = this.hourJobDao.queryDataDeviceHours(params);
        for (DataDeviceHour dataDeviceHour : list) {
            this.dataDeviceHourDao.insert(dataDeviceHour);
        }
		log.info(">>>>>>>>>>>>>>>>>deviceHourStatic 结束...");
        return ResResultBean.success();
    }
    
}
