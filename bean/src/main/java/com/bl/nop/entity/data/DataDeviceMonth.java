package com.bl.nop.entity.data;

import java.io.Serializable;
import java.util.Date;

public class DataDeviceMonth implements Serializable {
    private String deviceId;

    private Date statsDate;

    private Integer usedDuration;

    private Integer standDuration;

    private Integer personTime;

    private Integer depthTime;

    private static final long serialVersionUID = 1L;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Date getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(Date statsDate) {
        this.statsDate = statsDate;
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
}