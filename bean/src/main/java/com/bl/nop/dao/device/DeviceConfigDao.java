package com.bl.nop.dao.device;

import com.bl.nop.entity.device.DeviceConfig;

public interface DeviceConfigDao {
    int deleteByPrimaryKey(String deviceId);

    int insert(DeviceConfig record);

    int insertSelective(DeviceConfig record);

    DeviceConfig selectByPrimaryKey(String deviceId);

    int updateByPrimaryKeySelective(DeviceConfig record);

    int updateByPrimaryKey(DeviceConfig record);
}