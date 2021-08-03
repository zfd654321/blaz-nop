package com.bl.nop.dao.device;

import com.bl.nop.entity.device.DeviceGame;

public interface DeviceGameDao {
    int insert(DeviceGame record);

    int insertSelective(DeviceGame record);
}