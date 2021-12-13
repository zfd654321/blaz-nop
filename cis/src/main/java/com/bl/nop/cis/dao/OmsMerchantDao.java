package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.cis.dto.MerchantAdvertTypeDto;
import com.bl.nop.cis.dto.MerchantDeviceDto;
import com.bl.nop.cis.dto.MerchantGameDto;
import com.bl.nop.entity.merchant.Merchant;

public interface OmsMerchantDao {
    List<Merchant> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);

    List<MerchantDeviceDto> loadMerchantDevice(Map<String, Object> params);

    List<MerchantGameDto> loadMerchantGame(Map<String, Object> params);

    List<MerchantAdvertTypeDto> loadMerchantAdvert(Map<String, Object> params);

    int cleanMerchantDevice(Map<String, Object> params);

    int cleanMerchantGame(Map<String, Object> params);

    int cleanMerchantAdvert(Map<String, Object> params);
}
