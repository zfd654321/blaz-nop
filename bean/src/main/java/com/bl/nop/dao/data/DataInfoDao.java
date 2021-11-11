package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataInfo;

public interface DataInfoDao {
    int insert(DataInfo record);

    int insertSelective(DataInfo record);
}