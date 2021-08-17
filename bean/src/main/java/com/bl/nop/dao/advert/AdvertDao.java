package com.bl.nop.dao.advert;

import com.bl.nop.entity.advert.Advert;

public interface AdvertDao {
    int deleteByPrimaryKey(String id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKey(Advert record);
}