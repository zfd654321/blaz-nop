package com.bl.nop.dao.merchant;

import com.bl.nop.entity.merchant.MerchantAdvertType;

public interface MerchantAdvertTypeDao {
    int insert(MerchantAdvertType record);

    int insertSelective(MerchantAdvertType record);
}