package com.bl.nop.tts.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.data.DataDeviceDayDao;
import com.bl.nop.dao.data.DataDeviceHourDao;
import com.bl.nop.dao.data.DataDeviceMonthDao;
import com.bl.nop.dao.data.DataSumDayDao;
import com.bl.nop.entity.data.DataDeviceDay;
import com.bl.nop.entity.data.DataDeviceHour;
import com.bl.nop.entity.data.DataDeviceMonth;
import com.bl.nop.entity.data.DataSumDay;
import com.bl.nop.tts.dao.DeviceJobDao;
import com.bl.nop.tts.service.DeviceJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "DeviceJobService")
public class DeviceJobServiceImpl implements DeviceJobService {

    @Autowired
    private DeviceJobDao deviceJobDao;

    @Autowired
    private DataDeviceHourDao dataDeviceHourDao;
    @Autowired
    private DataDeviceDayDao dataDeviceDayDao;
    @Autowired
    private DataDeviceMonthDao dataDeviceMonthDao;
    @Autowired
    private DataSumDayDao dataSumDayDao;
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
        String statsDateStr=StringUtil.toStr(params.get("statsDate"));
        Date statsDate=DateUtil.strToDateShort(statsDateStr);
        this.deviceJobDao.cleanDataDeviceDay(params);
        List<DataDeviceDay> list = this.deviceJobDao.queryDataDeviceDay(params);
        Integer sumDevice=this.deviceJobDao.selectSumDevice(params);
        Integer onlineDevice=list.size();
        Integer onlineDuration=0;
        Integer usedDuration=0;
        Integer personTime=0;
        Integer depthTime=0;
        for (DataDeviceDay dataDeviceDay : list) {
            this.dataDeviceDayDao.insert(dataDeviceDay);
            onlineDuration+=(dataDeviceDay.getUsedDuration()+dataDeviceDay.getStandDuration());
            usedDuration+=dataDeviceDay.getUsedDuration();
            personTime+=dataDeviceDay.getPersonTime();
            depthTime+=dataDeviceDay.getDepthTime();
        }
        DataSumDay dataSumDay=new DataSumDay();
        dataSumDay.setStatsDate(statsDate);
        dataSumDay.setSumDevice(sumDevice);
        dataSumDay.setOnlineDevice(onlineDevice);
        dataSumDay.setOnlineDuration(onlineDuration);
        dataSumDay.setUsedDuration(usedDuration);
        dataSumDay.setPersonTime(personTime);
        dataSumDay.setDepthTime(depthTime);
        DataSumDay dataSumDayRepet=this.dataSumDayDao.selectByPrimaryKey(statsDate);
        if(dataSumDayRepet!=null){
            this.dataSumDayDao.updateByPrimaryKey(dataSumDay);
        }else{
            this.dataSumDayDao.insert(dataSumDay);
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
