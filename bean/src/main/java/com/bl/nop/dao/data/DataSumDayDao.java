package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataSumDay;
import java.util.Date;

public interface DataSumDayDao {
    int deleteByPrimaryKey(Date statsDate);

    int insert(DataSumDay record);

    int insertSelective(DataSumDay record);

    DataSumDay selectByPrimaryKey(Date statsDate);

    int updateByPrimaryKeySelective(DataSumDay record);

    int updateByPrimaryKey(DataSumDay record);
}