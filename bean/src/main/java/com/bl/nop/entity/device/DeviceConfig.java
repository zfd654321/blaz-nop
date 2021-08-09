package com.bl.nop.entity.device;

import java.io.Serializable;

public class DeviceConfig implements Serializable {
    private String deviceId;

    private String kinectLeftAndRightDis;

    private String kinectMinDis;

    private String kinectMaxDis;

    private String hostUrl;

    private String qrcodeUrl;

    private String logoUrl;

    private String watermarkUrl;

    private String themeName;

    private String gameEnterTime;

    private static final long serialVersionUID = 1L;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getKinectLeftAndRightDis() {
        return kinectLeftAndRightDis;
    }

    public void setKinectLeftAndRightDis(String kinectLeftAndRightDis) {
        this.kinectLeftAndRightDis = kinectLeftAndRightDis == null ? null : kinectLeftAndRightDis.trim();
    }

    public String getKinectMinDis() {
        return kinectMinDis;
    }

    public void setKinectMinDis(String kinectMinDis) {
        this.kinectMinDis = kinectMinDis == null ? null : kinectMinDis.trim();
    }

    public String getKinectMaxDis() {
        return kinectMaxDis;
    }

    public void setKinectMaxDis(String kinectMaxDis) {
        this.kinectMaxDis = kinectMaxDis == null ? null : kinectMaxDis.trim();
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl == null ? null : hostUrl.trim();
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl == null ? null : qrcodeUrl.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public String getWatermarkUrl() {
        return watermarkUrl;
    }

    public void setWatermarkUrl(String watermarkUrl) {
        this.watermarkUrl = watermarkUrl == null ? null : watermarkUrl.trim();
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName == null ? null : themeName.trim();
    }

    public String getGameEnterTime() {
        return gameEnterTime;
    }

    public void setGameEnterTime(String gameEnterTime) {
        this.gameEnterTime = gameEnterTime == null ? null : gameEnterTime.trim();
    }
}