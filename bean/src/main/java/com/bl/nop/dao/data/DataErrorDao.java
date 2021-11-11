package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataError;

public interface DataErrorDao {
    int insert(DataError record);

    int insertSelective(DataError record);
}