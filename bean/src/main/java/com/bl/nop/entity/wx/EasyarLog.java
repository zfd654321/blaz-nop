package com.bl.nop.entity.wx;

import java.io.Serializable;
import java.util.Date;

public class EasyarLog implements Serializable {
    private Integer id;

    private String type;

    private Integer status;

    private String targetId;

    private String name;

    private String clientIp;

    private Date statsTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public Date getStatsTime() {
        return statsTime;
    }

    public void setStatsTime(Date statsTime) {
        this.statsTime = statsTime;
    }
}