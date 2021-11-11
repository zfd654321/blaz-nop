package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataDeviceHour;

public interface DataDeviceHourDao {
    int insert(DataDeviceHour record);

    int insertSelective(DataDeviceHour record);
}