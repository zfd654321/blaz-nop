package com.bl.nop.entity.data;

import java.io.Serializable;
import java.util.Date;

public class DataGameDay implements Serializable {
    private String deviceId;

    private String gameId;

    private Date statsDate;

    private Integer finishTime;

    private Integer lostTime;

    private Integer breakTime;

    private static final long serialVersionUID = 1L;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId == null ? null : gameId.trim();
    }

    public Date getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(Date statsDate) {
        this.statsDate = statsDate;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getLostTime() {
        return lostTime;
    }

    public void setLostTime(Integer lostTime) {
        this.lostTime = lostTime;
    }

    public Integer getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(Integer breakTime) {
        this.breakTime = breakTime;
    }
}