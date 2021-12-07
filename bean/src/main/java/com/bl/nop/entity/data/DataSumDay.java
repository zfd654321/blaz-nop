package com.bl.nop.entity.data;

import java.io.Serializable;
import java.util.Date;

public class DataSumDay implements Serializable {
    private Date statsDate;

    private Integer sumDevice;

    private Integer onlineDevice;

    private Integer onlineDuration;

    private Integer usedDuration;

    private Integer personTime;

    private Integer depthTime;

    private static final long serialVersionUID = 1L;

    public Date getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(Date statsDate) {
        this.statsDate = statsDate;
    }

    public Integer getSumDevice() {
        return sumDevice;
    }

    public void setSumDevice(Integer sumDevice) {
        this.sumDevice = sumDevice;
    }

    public Integer getOnlineDevice() {
        return onlineDevice;
    }

    public void setOnlineDevice(Integer onlineDevice) {
        this.onlineDevice = onlineDevice;
    }

    public Integer getOnlineDuration() {
        return onlineDuration;
    }

    public void setOnlineDuration(Integer onlineDuration) {
        this.onlineDuration = onlineDuration;
    }

    public Integer getUsedDuration() {
        return usedDuration;
    }

    public void setUsedDuration(Integer usedDuration) {
        this.usedDuration = usedDuration;
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