package com.bl.nop.nis.util.easyar;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class UpdateTarget {

    private static final String TARGET_MGMT_URL = "http://cn1.crs.easyar.com:8888";
    private static final String CRS_APPID       = "--here is your CRS AppId--";
    private static final String API_KEY         = "--here is your API Key--";
    private static final String API_SECRET      = "--here is your API Secret--";
    private static final String IMAGE_PATH      = "test_target_image.jpg";
    private static final String TARGET_ID       = "my_targetid";

    public String update(Auth auth, String targetId, String imgPath) throws IOException {
        byte[] image      = Files.readAllBytes(Paths.get(imgPath));
        if(image.length > Common.MAXIMUM_SIZE) {
            System.err.println("maximum image size is 2MB");
            System.exit(-1);
        }
        JSONObject params = new JSONObject()
                .put("image", Base64.getEncoder().encodeToString(image))
                .put("name", "java-sdk-test-update");
        Auth.signParam(params, auth.getAppId(), auth.getApiKey(), auth.getApiSecret());
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), params.toString());
        Request request = new Request.Builder()
                .url(auth.getCloudURL() + "/target/" + targetId)
                .put(requestBody)
                .build();
        return new OkHttpClient.Builder().readTimeout(120, TimeUnit.SECONDS)
                .build().newCall(request).execute().body().string();

    }
    public static void main(String[] args) throws IOException {
        Auth accessInfo  =  new Auth(CRS_APPID, API_KEY, API_SECRET, TARGET_MGMT_URL);
        System.out.println(new UpdateTarget().update(accessInfo, TARGET_ID, IMAGE_PATH));

    }
}
