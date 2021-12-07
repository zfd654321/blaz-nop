package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.cis.dto.DeviceDataDto;
import com.bl.nop.cis.dto.SwitchDto;
import com.bl.nop.entity.data.DataSumDay;

public interface OmsDataDao {
    List<SwitchDto> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);

    List<DeviceDataDto> queryShowTopList(Map<String, Object> queryParams);

    List<DataSumDay> queryShowAllData(Map<String, Object> queryParams);
}
