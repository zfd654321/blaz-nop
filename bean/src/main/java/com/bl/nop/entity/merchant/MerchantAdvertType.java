package com.bl.nop.entity.merchant;

import java.io.Serializable;

public class MerchantAdvertType implements Serializable {
    private String merchantId;

    private Integer type;

    private static final long serialVersionUID = 1L;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}