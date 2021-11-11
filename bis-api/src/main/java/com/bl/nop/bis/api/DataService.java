package com.bl.nop.bis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface DataService {

    JSONObject heartData(Map<String, Object> params);

    JSONObject gameData(Map<String, Object> params);

    JSONObject errorData(Map<String, Object> params);


}
