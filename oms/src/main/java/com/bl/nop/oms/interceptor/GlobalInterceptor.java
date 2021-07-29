package com.bl.nop.oms.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.util.CommonConst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class GlobalInterceptor implements HandlerInterceptor {

	private final static Logger log = LoggerFactory.getLogger(GlobalInterceptor.class);
	
	private static final String[] URLS = {
			".html",
			".xls",
			".js",
			".css",
			".png",
			".jpg",
            "/login",
            "/imgcode"
    };
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURL().toString(); 
		log.info("开始拦截>>>>>>>>>>>>start>>>>>>>>>>>"+url);
		if(url.contains("?")) {
			url = url.split("?")[0];
		}
		for(String oneUrl : URLS){
            if(url.endsWith(oneUrl)){
                return true;
            }
        }
		
		Object obj = request.getSession().getAttribute(CommonConst.AUTHORIZATION);
		if(null == obj) {
			this.response(request, response);
			return false;
		}
		
		Map<String, String[]> params = request.getParameterMap();
		StringBuilder param = new StringBuilder();
		for(Map.Entry<String, String[]> entry:params.entrySet()) {
			String[] values = entry.getValue();
			param.append(entry.getKey()).append("=").append(null!=values&&values.length>0?values[0]:"").append("&");
		}
		SysUser user = (SysUser) obj;
		log.info("用户["+(null!=user?user.getUsername():"")+"]访问连接："+url+"?"+param.toString());
		return true;
	}

	private void response(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(ResResultBean.error("login", "请重新登陆").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("访问结束>>>>>>>>>>>");
	}
}
