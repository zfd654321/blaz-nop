package com.bl.nop.tts.service.impl;

import java.util.List;
import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.dao.data.DataDeviceDayDao;
import com.bl.nop.dao.data.DataDeviceHourDao;
import com.bl.nop.dao.data.DataDeviceMonthDao;
import com.bl.nop.entity.data.DataDeviceDay;
import com.bl.nop.entity.data.DataDeviceHour;
import com.bl.nop.entity.data.DataDeviceMonth;
import com.bl.nop.tts.dao.DeviceJobDao;
import com.bl.nop.tts.service.DeviceJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "HourJobService")
public class DeviceJobServiceImpl implements DeviceJobService {

    @Autowired
    private DeviceJobDao deviceJobDao;

    @Autowired
    private DataDeviceHourDao dataDeviceHourDao;
    @Autowired
    private DataDeviceDayDao dataDeviceDayDao;
    @Autowired
    private DataDeviceMonthDao dataDeviceMonthDao;
    private final static Logger log = LoggerFactory.getLogger(DeviceJobServiceImpl.class);

    @Override
    public ResResultBean deviceHourStatic(Map<String, Object> params) {
        log.info(">>>>>>>>>>>>>>>>>deviceHourStatic 执行...");
        this.deviceJobDao.cleanDataDeviceHours(params);
        List<DataDeviceHour> list = this.deviceJobDao.queryDataDeviceHours(params);
        for (DataDeviceHour dataDeviceHour : list) {
            this.dataDeviceHourDao.insert(dataDeviceHour);
        }
        log.info(">>>>>>>>>>>>>>>>>deviceHourStatic 结束...");
        return ResResultBean.success();
    }

    @Override
    public ResResultBean deviceDayStatic(Map<String, Object> params) {
        log.info(">>>>>>>>>>>>>>>>>deviceDayStatic 执行...");
        this.deviceJobDao.cleanDataDeviceDay(params);
        List<DataDeviceDay> list = this.deviceJobDao.queryDataDeviceDay(params);
        for (DataDeviceDay dataDeviceDay : list) {
            this.dataDeviceDayDao.insert(dataDeviceDay);
        }
        log.info(">>>>>>>>>>>>>>>>>deviceDayStatic 结束...");
        return ResResultBean.success();
    }

    @Override
    public ResResultBean deviceMonthStatic(Map<String, Object> params) {
        log.info(">>>>>>>>>>>>>>>>>deviceMonthStatic 执行...");
        this.deviceJobDao.cleanDataDeviceMonth(params);
        List<DataDeviceMonth> list = this.deviceJobDao.queryDataDeviceMonth(params);
        for (DataDeviceMonth dataDeviceMonth : list) {
            this.dataDeviceMonthDao.insert(dataDeviceMonth);
        }
        log.info(">>>>>>>>>>>>>>>>>deviceMonthStatic 结束...");
        return ResResultBean.success();
    }

}
