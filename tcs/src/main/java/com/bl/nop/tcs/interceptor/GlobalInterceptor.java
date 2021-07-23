package com.bl.nop.tcs.interceptor;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class GlobalInterceptor implements HandlerInterceptor {

	private final static Logger log = LoggerFactory.getLogger(GlobalInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("开始拦截>>>>>>>>>>>>start>>>>>>>>>>>");
		String url = request.getRequestURL().toString();
		Map<String, String[]> params = request.getParameterMap();
		StringBuilder param = new StringBuilder();
		for(Map.Entry<String, String[]> entry:params.entrySet()) {
			String[] values = entry.getValue();
			param.append(entry.getKey()).append("=").append(null!=values&&values.length>0?values[0]:"").append("&");
		}
		log.info("访问连接："+url+"?"+param.toString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
