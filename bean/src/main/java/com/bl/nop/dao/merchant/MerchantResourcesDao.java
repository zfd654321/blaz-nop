package com.bl.nop.dao.merchant;

import com.bl.nop.entity.merchant.MerchantResources;

public interface MerchantResourcesDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantResources record);

    int insertSelective(MerchantResources record);

    MerchantResources selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantResources record);

    int updateByPrimaryKey(MerchantResources record);
}