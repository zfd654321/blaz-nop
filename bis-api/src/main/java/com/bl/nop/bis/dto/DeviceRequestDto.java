package com.bl.nop.bis.dto;

import java.io.Serializable;

public class DeviceRequestDto  implements Serializable{
    private String request;
    private String filename;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
