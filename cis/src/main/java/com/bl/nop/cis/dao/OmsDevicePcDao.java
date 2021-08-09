package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.device.DevicePc;

public interface OmsDevicePcDao {
    List<DevicePc> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);
}
