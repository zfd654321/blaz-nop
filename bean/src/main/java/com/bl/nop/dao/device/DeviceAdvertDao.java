package com.bl.nop.dao.device;

import com.bl.nop.entity.device.DeviceAdvert;

public interface DeviceAdvertDao {
    int insert(DeviceAdvert record);

    int insertSelective(DeviceAdvert record);
}