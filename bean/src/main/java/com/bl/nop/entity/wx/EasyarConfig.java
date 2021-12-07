package com.bl.nop.entity.wx;

import java.io.Serializable;
import java.util.Date;

public class EasyarConfig implements Serializable {
    private Integer id;

    private String postUrl;

    private String serverUrl;

    private String cloudKey;

    private String cloudSecret;

    private String apiKey;

    private String apiSecret;

    private String token;

    private Date overtime;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl == null ? null : postUrl.trim();
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl == null ? null : serverUrl.trim();
    }

    public String getCloudKey() {
        return cloudKey;
    }

    public void setCloudKey(String cloudKey) {
        this.cloudKey = cloudKey == null ? null : cloudKey.trim();
    }

    public String getCloudSecret() {
        return cloudSecret;
    }

    public void setCloudSecret(String cloudSecret) {
        this.cloudSecret = cloudSecret == null ? null : cloudSecret.trim();
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey == null ? null : apiKey.trim();
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret == null ? null : apiSecret.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getOvertime() {
        return overtime;
    }

    public void setOvertime(Date overtime) {
        this.overtime = overtime;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}