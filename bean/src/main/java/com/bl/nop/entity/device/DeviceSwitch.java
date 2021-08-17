package com.bl.nop.entity.device;

import java.io.Serializable;
import java.util.Date;

public class DeviceSwitch implements Serializable {
    private String deviceId;

    private Integer software;

    private Integer pay;

    private Integer statistics;

    private Integer onlinecheck;

    private Integer filecheck;

    private Date shutdown;

    private Date restart;

    private static final long serialVersionUID = 1L;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Integer getSoftware() {
        return software;
    }

    public void setSoftware(Integer software) {
        this.software = software;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public Integer getStatistics() {
        return statistics;
    }

    public void setStatistics(Integer statistics) {
        this.statistics = statistics;
    }

    public Integer getOnlinecheck() {
        return onlinecheck;
    }

    public void setOnlinecheck(Integer onlinecheck) {
        this.onlinecheck = onlinecheck;
    }

    public Integer getFilecheck() {
        return filecheck;
    }

    public void setFilecheck(Integer filecheck) {
        this.filecheck = filecheck;
    }

    public Date getShutdown() {
        return shutdown;
    }

    public void setShutdown(Date shutdown) {
        this.shutdown = shutdown;
    }

    public Date getRestart() {
        return restart;
    }

    public void setRestart(Date restart) {
        this.restart = restart;
    }
}