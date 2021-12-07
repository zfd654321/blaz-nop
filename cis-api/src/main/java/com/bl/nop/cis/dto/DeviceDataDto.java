package com.bl.nop.cis.dto;

import java.io.Serializable;
import java.util.Date;

public class DeviceDataDto implements Serializable {
    private String deviceId;
    private String pcId;
    private String name;
    private Integer usedDuration;
    private Integer standDuration;
    private Integer personTime;
    private Integer depthTime;
    private Date lastHeart;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUsedDuration() {
        return usedDuration;
    }

    public void setUsedDuration(Integer usedDuration) {
        this.usedDuration = usedDuration;
    }

    public Integer getStandDuration() {
        return standDuration;
    }

    public void setStandDuration(Integer standDuration) {
        this.standDuration = standDuration;
    }

    public Integer getPersonTime() {
        return personTime;
    }

    public void setPersonTime(Integer personTime) {
        this.personTime = personTime;
    }

    public Integer getDepthTime() {
        return depthTime;
    }

    public void setDepthTime(Integer depthTime) {
        this.depthTime = depthTime;
    }

    public Date getLastHeart() {
        return lastHeart;
    }

    public void setLastHeart(Date lastHeart) {
        this.lastHeart = lastHeart;
    }

}
