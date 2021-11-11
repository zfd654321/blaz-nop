package com.bl.nop.dao.data;

import com.bl.nop.entity.data.DataGame;

public interface DataGameDao {
    int insert(DataGame record);

    int insertSelective(DataGame record);
}