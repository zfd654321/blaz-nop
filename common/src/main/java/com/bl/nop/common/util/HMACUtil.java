package com.bl.nop.common.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec; 
  
/** 
 * HMAC_SHA1 Sign生成器. 
 *  
 * 需要apache.commons.codec包 
 *  
 */  
public class HMACUtil {  
  
    /** 
     * 使用 HMAC-SHA1 签名方法对data进行签名 
     *  
     * @param data 
     *            被签名的字符串 
     * @param key 
     *            密钥      
     * @return  
                      加密后的字符串 
     */  
    public static String HMACSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

        }

        return sb.toString().toUpperCase();

    }
}  