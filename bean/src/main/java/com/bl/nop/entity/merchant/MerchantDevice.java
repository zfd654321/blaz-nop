package com.bl.nop.entity.merchant;

import java.io.Serializable;

public class MerchantDevice implements Serializable {
    private String merchantId;

    private String deviceId;

    private static final long serialVersionUID = 1L;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }
}