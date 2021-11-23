package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataGameDay;

public interface DataGameDayDao {
    int insert(DataGameDay record);

    int insertSelective(DataGameDay record);
}