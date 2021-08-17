package com.bl.nop.dao.advert;

import com.bl.nop.entity.advert.AdvertType;

public interface AdvertTypeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AdvertType record);

    int insertSelective(AdvertType record);

    AdvertType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdvertType record);

    int updateByPrimaryKey(AdvertType record);
}