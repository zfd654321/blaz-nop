package com.bl.nop.bis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface ConfigService {

    public JSONObject list(Map<String, Object> params);

    public JSONObject deviceConfig(Map<String, Object> params);

    public JSONObject deviceSwitch(Map<String, Object> params);

    public JSONObject advert(Map<String, Object> params);

    public JSONObject game(Map<String, Object> params);

    public JSONObject gameNetres(Map<String, Object> params);

}
