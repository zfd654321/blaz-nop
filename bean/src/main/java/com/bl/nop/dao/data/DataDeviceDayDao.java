package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataDeviceDay;

public interface DataDeviceDayDao {
    int insert(DataDeviceDay record);

    int insertSelective(DataDeviceDay record);
}