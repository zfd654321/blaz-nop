package com.bl.nop.dao.device;

import com.bl.nop.entity.device.DeviceRequest;

public interface DeviceRequestDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceRequest record);

    int insertSelective(DeviceRequest record);

    DeviceRequest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceRequest record);

    int updateByPrimaryKey(DeviceRequest record);
}