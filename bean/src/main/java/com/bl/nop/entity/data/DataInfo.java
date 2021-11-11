package com.bl.nop.entity.data;

import java.io.Serializable;
import java.util.Date;

public class DataInfo implements Serializable {
    private String deviceId;

    private Date dataTime;

    private Date statsDate;

    private Integer statsHour;

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

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Date getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(Date statsDate) {
        this.statsDate = statsDate;
    }

    public Integer getStatsHour() {
        return statsHour;
    }

    public void setStatsHour(Integer statsHour) {
        this.statsHour = statsHour;
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