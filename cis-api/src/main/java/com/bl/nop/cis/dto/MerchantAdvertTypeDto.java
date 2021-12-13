package com.bl.nop.cis.dto;

import com.bl.nop.entity.advert.AdvertType;

public class MerchantAdvertTypeDto extends AdvertType {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String merchantId;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

}
