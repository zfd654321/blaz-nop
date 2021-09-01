package com.bl.nop.wxs.controller.common;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.common.exception.BusinessException;

public class BaseController {
	protected static Logger log = LoggerFactory.getLogger(BaseController.class);
	
	/**
     * 将返回的信息转化为JSON格式。
     */
    public static void writeResponseByJson(HttpServletRequest request,
			HttpServletResponse response, JSONObject oj){
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(oj.toString());
		} catch (IOException e) { 
			e.printStackTrace();
		}
    }
    
    public static void writeResponseByString(HttpServletRequest request,
			HttpServletResponse response, String resultStr){
    	response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(resultStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 从请求头报文中取出请求的参数放入Map中
     * @param request
     * @param paramsMap
     * @return
     */
    public static Map<String, Object> buildParamsMap(HttpServletRequest request){
    	Map<String, Object> paramsMap = new HashMap<>();
		Enumeration<?> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = (String) paramNames.nextElement();
			paramsMap.put(key, request.getParameter(key));
		}
		log.info("请求数据为："+paramsMap);
		paramsMap.put("request_ip", getRemoteHost(request));
		return paramsMap;
	}

	/**
     * 从请求头报文中取出请求的参数放入Map中(含附件)
     * @param request
     * @param paramsMap
     * @return
     */
    public static Map<String, Object> buildMutipartParamsMap(HttpServletRequest request){
    	Map<String, Object> paramsMap = new HashMap<>();
		Enumeration<?> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = (String) paramNames.nextElement();
			paramsMap.put(key, request.getParameter(key));
		}
		log.info("请求数据为："+paramsMap);
		//得到请求中的附件
		MultipartHttpServletRequest mureq = null;
		try {
			mureq = (MultipartHttpServletRequest) request;
			paramsMap.put("mureq", mureq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mureq != null) {
			List<MultipartFile> list = mureq.getFiles("file");
			paramsMap.put("filelist", list);
			log.info("请求数据为："+paramsMap+"有附件");
		}
		paramsMap.put("request_ip", getRemoteHost(request));
		return paramsMap;
	}
	
    /**
     * 得到远程的请求机器的IP
     * @param request
     * @return
     */
	public static String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	
   @ExceptionHandler
   @ResponseBody
   public void handleAndReturnData(HttpServletRequest request, HttpServletResponse response, Exception ex) {
	   log.error("请求数据失败", ex);
       if(ex instanceof BusinessException) {
           BusinessException e = (BusinessException)ex;
           JSONObject oj = new JSONObject();
           oj.put("returnCode", e.getCode());
		   oj.put("returnMsg",  e.getMessage());
           writeResponseByJson(request, response, oj);
           return;
       } 
       JSONObject oj = new JSONObject();
       oj.put("returnCode", "-100");
		oj.put("returnMsg", "操作失败");
       writeResponseByJson(request, response, oj);
   }
}
