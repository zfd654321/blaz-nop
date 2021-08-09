package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;

public interface OmsDeviceDao {
    List<Device> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);

    List<DevicePc> freePcList(Map<String, Object> params);
}
