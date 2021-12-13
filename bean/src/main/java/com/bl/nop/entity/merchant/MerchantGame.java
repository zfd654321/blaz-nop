package com.bl.nop.entity.merchant;

import java.io.Serializable;

public class MerchantGame implements Serializable {
    private String merchantId;

    private String gameId;

    private static final long serialVersionUID = 1L;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId == null ? null : gameId.trim();
    }
}