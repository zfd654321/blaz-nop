package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataOnline;

public interface DataOnlineDao {
    int deleteByPrimaryKey(String deviceId);

    int insert(DataOnline record);

    int insertSelective(DataOnline record);

    DataOnline selectByPrimaryKey(String deviceId);

    int updateByPrimaryKeySelective(DataOnline record);

    int updateByPrimaryKey(DataOnline record);
}