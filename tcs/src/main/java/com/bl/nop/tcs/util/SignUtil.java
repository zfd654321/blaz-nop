package com.bl.nop.tcs.util;

import java.security.MessageDigest;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

public class SignUtil {
	
	public static JSONObject getJsSdkSign(final String url) {
		final JSONObject json = new JSONObject();
		final String noncestr = create_nonce_str();
		final String tsapiTicket = WeixinUtil.getTicket();
		final String timestamp = create_timestamp();
		json.put("noncestr", noncestr);
		json.put("timestamp", timestamp);
		json.put("jsapi_ticket", tsapiTicket);
		json.put("appId", WeixinUtil.appid);
		final String signature = getJsSdkSign(noncestr, tsapiTicket, timestamp, url);
		if(null == signature) {
			return null;
		}
		json.put("signature", signature);
		System.out.println(json.toJSONString());
		return json;
	}
	public static String getJsSdkSign(final String noncestr,final String tsapiTicket,final String timestamp,final String url){
		final String content="jsapi_ticket="+tsapiTicket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
		System.out.println(content);
		String ciphertext=null;
		try {
			final MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(content.getBytes("UTF-8"));
			final byte[] digest = crypt.digest();
			ciphertext=byteToStr(digest);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return ciphertext;
	}
	
	public static String byteToStr(final byte[] byteArray){
		String strDigest="";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest+=byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}
	
	public static String byteToHexStr(final byte mByte){
		final char[] Digit={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		final char[] tempArr = new char[2];
		tempArr[0]=Digit[(mByte>>>4)&0X0F];
		tempArr[1]=Digit[mByte&0X0F];
		final String s=new String(tempArr);
		return s;
	}
	
	private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
 
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
