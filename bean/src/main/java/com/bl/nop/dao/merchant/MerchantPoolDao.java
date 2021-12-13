package com.bl.nop.dao.merchant;

import com.bl.nop.entity.merchant.MerchantPool;

public interface MerchantPoolDao {
    int deleteByPrimaryKey(String id);

    int insert(MerchantPool record);

    int insertSelective(MerchantPool record);

    MerchantPool selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MerchantPool record);

    int updateByPrimaryKey(MerchantPool record);
}