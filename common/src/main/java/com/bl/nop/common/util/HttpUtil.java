package com.bl.nop.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	
	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	private static final int TIMEOUT = 10000;// 连接超时时间，默认10秒
    private static final int CONN_TIMEOUT = 30000;// 传输超时时间，默认30秒
    private static final String ENCODING = "UTF-8";
    /**
     * get获取信息
     * @param url
     * @return
     */
	public static JSONObject getJson(String url) {
		JSONObject jsonObject = null;
		try {
			String result = get(url);
			if(!StringUtils.isBlank(result)){
				jsonObject = JSONObject.parseObject(result);
				return jsonObject;
			}
		} catch (Exception e) {
			log.error("httpclient getJson error", e);
		}
		
        return jsonObject;
    }
	/**
	 * post获取信息
	 * @param url
	 * @return
	 */
	public static JSONObject postJson(String url, Map<String, Object> params) {
		JSONObject jsonObject = null;
		try {
			String result = post(url, params);
			if(!StringUtils.isBlank(result)){
				jsonObject = JSONObject.parseObject(result);
				return jsonObject;
			}
		} catch (Exception e) {
			log.error("httpclient postJson error", e);
		}
		
		return jsonObject;
	}
	/**
	 * post请求数据
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, Map<String, Object> params) throws ClientProtocolException, IOException {
		log.info("Post Request Data, Url："+url+", params:"+params);
		String result = null;
		//首先初始化HttpClient对象
		HttpClient client = HttpClientBuilder.create().build();
		//通过get方式进行提交
		HttpPost httpPost = new HttpPost(url);
		
		if(null != params && !params.isEmpty()) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
	        for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String value = String.valueOf(params.get(name));
				nvps.add(new BasicNameValuePair(name, value));
			}
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
		}

		//通过HTTPclient的execute方法进行发送请求
		HttpResponse response = client.execute(httpPost);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			//从response里面拿自己想要的结果
			HttpEntity entity = response.getEntity();
			if(entity != null){
				result = EntityUtils.toString(entity, ENCODING);
			}
		}
		//把链接释放掉
		httpPost.releaseConnection();
		return result;
	}
	/**
	 * body传参
	 * @param requestUrl
	 * @param data
	 * @return
	 */
	public static String body(String requestUrl, String data) {
		InputStream in = null;
		OutputStream out = null;
		BufferedReader read = null;
		String result = "";
		try {
			log.info("Body请求连接："+requestUrl+"，请求参数："+data);
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(100000);
			conn.setReadTimeout(100000);
			conn.setUseCaches(false);
			conn.setDoInput(true);

			conn.setDoOutput(true);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", ENCODING);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty(
					"user-agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");
			conn.setRequestMethod("POST");
			out = conn.getOutputStream();
			byte[] request_data = data.getBytes(ENCODING); 
			out.write(request_data);
			out.flush();
			
			in = conn.getInputStream();
			read = new BufferedReader(new InputStreamReader(in, ENCODING));
			StringBuffer bufferRes = new StringBuffer();
			String valueStr=null;
			while((valueStr=read.readLine())!=null){
				bufferRes.append(valueStr);
			}
			result=bufferRes.toString();
		} catch (Exception e) {
			log.error("请求失败", e);
		} finally {
			try {
				if(null != in) {
						in.close();
				}
				if(null != read) {
					read.close();
				}
				if(null != out) {
					out.close();
				}
			} catch (IOException e) {
				log.error("请求连接，关闭连接失败", e);
			}
		}
		
		return result;
	}
	/**
	 * https请求
	 * @param url
	 * @param content
	 * @param charset
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static byte[] bodySSL(String url, String content)
			throws NoSuchAlgorithmException, KeyManagementException,
			IOException {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
				new java.security.SecureRandom());

		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setDoOutput(true);
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content.getBytes(ENCODING));
		// 刷新、关闭
		out.flush();
		out.close();
		InputStream is = conn.getInputStream();
		if (is != null) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			is.close();
			return outStream.toByteArray();
		}
		return null;
	}
	
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}
	
	/**
	 * get获取数据
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url) throws ClientProtocolException, IOException {
		log.info("Get Request Data, Url："+url);
		String result = null;
		//首先初始化HttpClient对象
		HttpClient client = HttpClientBuilder.create().build();
		//通过get方式进行提交
		HttpGet httpGet = new HttpGet(url);
		//通过HTTPclient的execute方法进行发送请求
		HttpResponse response = client.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            /**读取服务器返回过来的json字符串数据**/
			result = EntityUtils.toString(response.getEntity(), ENCODING);
        }
		//把链接释放掉
		httpGet.releaseConnection();
		return result;
	}
	/**
	 * 上传数据
	 * @param posturl
	 * @param data
	 * @return
	 */
	public static String upload(String posturl, byte[] data) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(posturl);
		post.setHeader( "User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0");
		post.setHeader("Host", "file.api.weixin.qq.com");
		post.setHeader("Connection", "Keep-Alive");
		post.setHeader("Cache-Control", "no-cache");
		String result = null;
		ByteArrayEntity postEntity = new ByteArrayEntity(data);
		post.setEntity(postEntity);
	        // 根据默认超时限制初始化requestConfig
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(CONN_TIMEOUT).build();
	        // 设置请求器的配置
		post.setConfig(requestConfig);
	    try {
            HttpResponse response = null;
            try {
            	log.info("httpPost是否为空"+(null == post));
            	log.info("httpClient是否为空"+(null == client));
                response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            try {
                result = EntityUtils.toString(entity, "UTF-8");
                log.info("上传文件返回结果："+result);
            } catch (IOException e) {
                e.printStackTrace();
            }
	    } finally {
	    	post.abort();
	    }
		return result;
	}
	
}
