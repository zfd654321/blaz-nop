package com.bl.nop.bis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface DownloadService {

    public JSONObject version(Map<String, Object> params);

    public JSONObject game(Map<String, Object> params);

    public JSONObject resources(Map<String, Object> params);

}
