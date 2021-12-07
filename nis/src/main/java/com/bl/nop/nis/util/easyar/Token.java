package com.bl.nop.nis.util.easyar;

import java.io.IOException;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Token {

    private static final String API_KEY         = "0e76c720a3bd096eae30de9b32f06709";
    private static final String API_SECRET      = "11548021ff5788a2d608120e1a747e3016ab9a4c76651f31c82dd8b52019e159";

    public String token(Auth auth) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        JSONObject params = new JSONObject();
        params.put("expires", 86400);
        Auth.signParam(params, auth.getApiKey(), auth.getApiSecret());
        System.out.println(params.toString());
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), params.toString());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://uac.easyar.com/token")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
        Auth accessInfo  =  new Auth(API_KEY, API_SECRET);
        System.out.println(new Token().token(accessInfo));
    }
}
