package com.bl.nop.dao.device;

import com.bl.nop.entity.device.DeviceLog;

public interface DeviceLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceLog record);

    int insertSelective(DeviceLog record);

    DeviceLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceLog record);

    int updateByPrimaryKey(DeviceLog record);
}