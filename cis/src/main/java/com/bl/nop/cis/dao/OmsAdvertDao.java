package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.advert.Advert;
import com.bl.nop.entity.advert.AdvertType;

public interface OmsAdvertDao {
    List<Advert> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);
    
    List<AdvertType> findTypeItemByPage(Map<String, Object> param);

    int findTypeItemCount(Map<String, Object> param);
}
