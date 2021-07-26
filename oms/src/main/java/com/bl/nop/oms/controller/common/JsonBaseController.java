package com.bl.nop.oms.controller.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.exception.BusinessException;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.util.CommonConst;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class JsonBaseController {

    @ExceptionHandler
    @ResponseBody
    public ResResultBean  handleAndReturnData(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        if(ex instanceof BusinessException) {
            BusinessException e = (BusinessException)ex;
            return ResResultBean.error(e.getCode(), e.getMessage());
        }
        return ResResultBean.error();
    }
    
    protected SysUser getUser(HttpServletRequest request) {
    	SysUser user = (SysUser)request.getSession().getAttribute(CommonConst.AUTHORIZATION);
    	return user;
    }
    protected void setUser(HttpServletRequest request, Object user) {
    	request.getSession().setAttribute(CommonConst.AUTHORIZATION, user);
    }
    protected void clearSession(HttpServletRequest request) {
    	request.getSession().removeAttribute(CommonConst.AUTHORIZATION);
    }
    protected Map<String, Object> getParameterMap(HttpServletRequest request) {
    	Map<String, String[]> map=request.getParameterMap();  
        Set<Map.Entry<String, String[]>> keSet = map.entrySet();  
        Map<String, Object> retMap = new HashMap<>();
        for(Iterator<Map.Entry<String, String[]>> itr=keSet.iterator();itr.hasNext();){  
            Map.Entry<String, String[]> me = itr.next();  
            String ok = me.getKey();  
            String[] ov = me.getValue();  
            if(ov != null && ov.length > 0) {
            	String value = "";  
            	int i = 0;
            	for(String obj:ov) {
            		if(obj != null && !"".equals(obj)) {
            			if(i > 0) {
            				value += ",";
            			}
            			value += obj;
            			i++;
            		}
            	}
            	retMap.put(ok, value);
            }
        }
        return retMap;
	}
}
