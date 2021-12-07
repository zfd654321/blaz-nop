package com.bl.nop.nis.util.easyar;

import okhttp3.OkHttpClient;
import org.json.JSONObject;
import java.io.IOException;

public class GetTarget {

    private static final String TARGET_MGMT_URL = "http://cn1.crs.easyar.com:8888";
    private static final String CRS_APPID       = "--here is your CRS AppId--";
    private static final String API_KEY         = "--here is your API Key--";
    private static final String API_SECRET      = "--here is your API Secret--";
    private static final String TARGET_ID       = "my_targetid";

    public String getTarget(Auth auth, String targetId) throws IOException{
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(auth.getCloudURL() + "/target/"+targetId+"?"+ Common.toParam(
                        Auth.signParam(new JSONObject(), auth.getAppId(), auth.getApiKey(), auth.getApiSecret())
                ))
                .get()
                .build();
        return new OkHttpClient.Builder().build().newCall(request).execute().body().string();
    }

    public static void main(String[] args) throws IOException{
        Auth accessInfo  =  new Auth(CRS_APPID, API_KEY, API_SECRET, TARGET_MGMT_URL);
        System.out.println(new GetTarget().getTarget(accessInfo, TARGET_ID));
    }

}
