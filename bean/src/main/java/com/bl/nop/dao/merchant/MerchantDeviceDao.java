package com.bl.nop.dao.merchant;

import com.bl.nop.entity.merchant.MerchantDevice;

public interface MerchantDeviceDao {
    int insert(MerchantDevice record);

    int insertSelective(MerchantDevice record);
}