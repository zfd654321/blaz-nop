package com.bl.nop.entity.device;

import java.io.Serializable;

public class DeviceGameNetres implements Serializable {
    private String deviceId;

    private String netresId;

    private String resUrl;

    private static final long serialVersionUID = 1L;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getNetresId() {
        return netresId;
    }

    public void setNetresId(String netresId) {
        this.netresId = netresId == null ? null : netresId.trim();
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl == null ? null : resUrl.trim();
    }
}