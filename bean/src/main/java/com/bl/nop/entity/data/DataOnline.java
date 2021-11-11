package com.bl.nop.entity.data;

import java.io.Serializable;
import java.util.Date;

public class DataOnline implements Serializable {
    private String deviceId;

    private Date lastHeart;

    private Date lastGame;

    private static final long serialVersionUID = 1L;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Date getLastHeart() {
        return lastHeart;
    }

    public void setLastHeart(Date lastHeart) {
        this.lastHeart = lastHeart;
    }

    public Date getLastGame() {
        return lastGame;
    }

    public void setLastGame(Date lastGame) {
        this.lastGame = lastGame;
    }
}