package com.bl.nop.tts.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.data.DataDeviceDay;
import com.bl.nop.entity.data.DataDeviceHour;
import com.bl.nop.entity.data.DataDeviceMonth;

import org.springframework.stereotype.Repository;

@Repository
public interface DeviceJobDao {

    List<DataDeviceHour> queryDataDeviceHours(Map<String, Object> params);

    List<DataDeviceDay> queryDataDeviceDay(Map<String, Object> params);

    List<DataDeviceMonth> queryDataDeviceMonth(Map<String, Object> params);

    int cleanDataDeviceHours(Map<String, Object> params);

    int cleanDataDeviceDay(Map<String, Object> params);

    int cleanDataDeviceMonth(Map<String, Object> params);

    Integer selectSumDevice(Map<String, Object> params);

}
