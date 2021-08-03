package com.bl.nop.dao.device;

import com.bl.nop.entity.device.DevicePc;

public interface DevicePcDao {
    int deleteByPrimaryKey(String id);

    int insert(DevicePc record);

    int insertSelective(DevicePc record);

    DevicePc selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DevicePc record);

    int updateByPrimaryKey(DevicePc record);
}