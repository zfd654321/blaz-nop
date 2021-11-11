package com.bl.nop.tts.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.data.DataDeviceHour;

import org.springframework.stereotype.Repository;

@Repository
public interface HourJobDao {

    List<DataDeviceHour> queryDataDeviceHours(Map<String, Object> map);

}
