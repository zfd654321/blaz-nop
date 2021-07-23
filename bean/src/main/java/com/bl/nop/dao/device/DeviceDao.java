package com.bl.nop.dao.device;

import com.bl.nop.entity.device.Device;

public interface DeviceDao {
    int insert(Device record);

    int insertSelective(Device record);
}