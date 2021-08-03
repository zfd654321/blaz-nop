package com.bl.nop.dao.device;

import com.bl.nop.entity.device.DeviceSwitch;

public interface DeviceSwitchDao {
    int deleteByPrimaryKey(String deviceId);

    int insert(DeviceSwitch record);

    int insertSelective(DeviceSwitch record);

    DeviceSwitch selectByPrimaryKey(String deviceId);

    int updateByPrimaryKeySelective(DeviceSwitch record);

    int updateByPrimaryKey(DeviceSwitch record);
}