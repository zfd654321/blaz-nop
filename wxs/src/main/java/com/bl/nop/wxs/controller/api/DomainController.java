package com.bl.nop.wxs.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.nis.api.DomainService;
import com.bl.nop.wxs.controller.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/domain")
public class DomainController extends BaseController {

    @Autowired
    private DomainService domainService;

    /**
     * 获取用户openid
     * 
     * @param request
     * @param response
     */
	@RequestMapping("/getWXUserOpenId")
	public void getWXUserOpenId(HttpServletResponse response, HttpServletRequest request) {
        String code = request.getParameter("code");
		log.info("获取用户openid");
		JSONObject oj = domainService.getWXUserOpenId(code); 
        log.info("获取用户openid，返回数据：" + oj.toJSONString());
		writeResponseByJson(request, response, oj);
	}

    /**
     * 更新用户个人信息
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/userData")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备接口列表");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = domainService.userData(params);
        log.info("[" + pcId + "]获取设备接口列表，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }


}
