package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataDeviceMonth;

public interface DataDeviceMonthDao {
    int insert(DataDeviceMonth record);

    int insertSelective(DataDeviceMonth record);
}