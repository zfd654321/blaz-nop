package com.bl.nop.dao.merchant;

import com.bl.nop.entity.merchant.Merchant;

public interface MerchantDao {
    int deleteByPrimaryKey(String id);

    int insert(Merchant record);

    int insertSelective(Merchant record);

    Merchant selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Merchant record);

    int updateByPrimaryKey(Merchant record);
}