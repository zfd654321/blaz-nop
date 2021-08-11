package com.bl.nop.entity.game;

import java.io.Serializable;

public class GameNetres implements Serializable {
    private String id;

    private String gameId;

    private String name;

    private String property;

    private String type;

    private String defaulturl;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId == null ? null : gameId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property == null ? null : property.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDefaulturl() {
        return defaulturl;
    }

    public void setDefaulturl(String defaulturl) {
        this.defaulturl = defaulturl == null ? null : defaulturl.trim();
    }
}