package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.cis.dto.SwitchDto;

public interface OmsDeviceSwitchDao {
    List<SwitchDto> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);
}
