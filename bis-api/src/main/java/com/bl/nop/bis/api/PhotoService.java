package com.bl.nop.bis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface PhotoService {

    public JSONObject uploadPhoto(Map<String, Object> params);

    public JSONObject getQrCode(Map<String, Object> params);

}
