package com.bl.nop.dao.merchant;

import com.bl.nop.entity.merchant.MerchantGame;

public interface MerchantGameDao {
    int insert(MerchantGame record);

    int insertSelective(MerchantGame record);
}