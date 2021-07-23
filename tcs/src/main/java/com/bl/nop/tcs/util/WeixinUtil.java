package com.bl.nop.tcs.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.common.exception.BusinessException;
import com.bl.nop.common.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinUtil {
    
	private static final Logger log = LoggerFactory.getLogger(WeixinUtil.class);
	public static String appid = "";
	public static String appSecret = "";
	
	private static WeixinAccessToken weixinAccessToken = new WeixinAccessToken();
	private static WeixinTicket weixinTicket = new WeixinTicket();
	
	static {
		appid = PropertyUtil.getProperty("weixin.appid");
		appSecret = PropertyUtil.getProperty("weixin.appsecret");
	}
	
	public synchronized static String getAccessToken(){
		if(null == weixinAccessToken || weixinAccessToken.isOvertime()) {
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appSecret+"";
			JSONObject backData = HttpUtil.getJson(url);
			if(null == backData) {
				throw new BusinessException("获取token失败", "01");
			}
			log.info("获取access_token："+backData.toJSONString());
			String accessToken = backData.getString("access_token");
			int expires_in = backData.getIntValue("expires_in");
			weixinAccessToken.setAccess_token(accessToken);
			weixinAccessToken.setExpires_in(expires_in);
		}
		
		return weixinAccessToken.getAccess_token();
	}
	
	public synchronized static String getTicket() {
		if(null == weixinTicket || weixinTicket.isOvertime()) {
			String token = getAccessToken();
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
			String url1 = url.replace("ACCESS_TOKEN", token);
			JSONObject backData = HttpUtil.getJson(url1);
			if(null == backData) {
				throw new BusinessException("获取ticket失败", "02");
			}
			log.info("获取ticket："+backData.toJSONString());
			String errmsg = backData.getString("errmsg");
			if(!"ok".equalsIgnoreCase(errmsg)) {
				throw new BusinessException("获取ticket失败", "03");
			}
			String ticket = backData.getString("ticket");
			int expires_in = backData.getIntValue("expires_in");
			weixinTicket.setTicket(ticket);
			weixinTicket.setExpires_in(expires_in);
		}
		return weixinTicket.getTicket();
	}
	
	public static String uploadFodder(File file) {
		String access_token = getAccessToken();
		String posturl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="+access_token+"&type=image";
		String res = httpRequest(posturl, file);
		log.info("上传临时素材："+res);
		return res;
	}
	
	public static String httpRequest(String requestUrl, File file) {  
        StringBuffer buffer = new StringBuffer();  
    
        try{
            //1.建立连接
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  //打开链接
            
            //1.1输入输出设置
            httpUrlConn.setDoInput(true);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setUseCaches(false); // post方式不能使用缓存
            //1.2设置请求头信息
            httpUrlConn.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("Charset", "UTF-8");
            //1.3设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            httpUrlConn.setRequestProperty("Content-Type","multipart/form-data; boundary="+ BOUNDARY);

            // 请求正文信息
            // 第一部分：
            //2.将文件头输出到微信服务器
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length()
                    + "\";filename=\""+ file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());
            // 将表头写入输出流中：输出表头
            outputStream.write(head);

            //3.将文件正文部分输出到微信服务器
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            in.close();
            //4.将结尾部分输出到微信服务器
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            outputStream.write(foot);
            outputStream.flush();
            outputStream.close();

            
            //5.将微信服务器返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
            
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  

        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } 
        return buffer.toString();
    }

	public static JSONObject uploadUrl(String uri, String type) {
		JSONObject result = null;
		URL url = null;
		InputStream is = null;
		OutputStream os = null;
        try {
        	url = new URL(uri);
        	is = new BufferedInputStream(url.openStream());
        	String tempPath =System.getProperty("java.io.tmpdir");
        	String file = tempPath + +File.separatorChar + UUID.randomUUID().toString() + type;
        	System.out.println(file);
        	os = new FileOutputStream(file); 
            byte[] buff = new byte[1024]; 
            int bytesRead; 
            while (-1 != (bytesRead = is.read(buff, 0, buff.length))) { 
              os.write(buff, 0, bytesRead); 
            } 
            if (is != null) 
              is.close(); 
            if (os != null) 
              os.close();
            
            String res = uploadFodder(new File(file));
            result = JSONObject.parseObject(res);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传图片失败", e);
        } 
        return result;
	}
	
	public static void main(String[] args) {
		/*String access_token = getAccessToken();
		System.out.println(access_token);*/
		URL url = null;
		InputStream is = null;
		OutputStream os = null;
        try {
        	url = new URL("http://192.168.10.200:9092/BLFiles/attachment/APP/V3.0/GravityLens/image/BLImageXXX7551700220180206120547.jpg");
        	is = new BufferedInputStream(url.openStream());
        	String tempPath =System.getProperty("java.io.tmpdir");
        	String file = tempPath + +File.separatorChar + UUID.randomUUID().toString() + ".jpg";
        	System.out.println(file);
        	os = new FileOutputStream(file); 
            byte[] buff = new byte[1024]; 
            int bytesRead; 
            while (-1 != (bytesRead = is.read(buff, 0, buff.length))) { 
              os.write(buff, 0, bytesRead); 
            } 
            if (is != null) 
              is.close(); 
            if (os != null) 
              os.close();
            
            uploadFodder(new File(file));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
}
