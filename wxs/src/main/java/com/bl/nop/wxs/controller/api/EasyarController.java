package com.bl.nop.wxs.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.nis.api.EasyArService;
import com.bl.nop.wxs.controller.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/easyar")
public class EasyarController extends BaseController {

    @Autowired
    private EasyArService easyArService;

    /**
     * 获取token
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/token")
    public void bondPhoto(HttpServletResponse response, HttpServletRequest request) {
        log.info("easyAr功能获取token");
        JSONObject oj = easyArService.token();
        log.info("easyAr功能获取token，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 保存识别日志
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/savelog")
    public void savelog(HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> params = buildParamsMap(request);
        log.info("easyAr功能获取token");
        JSONObject oj = easyArService.savelog(params);
        log.info("easyAr功能获取token，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 查询识别日志
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/list")
    public void list(HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> params = buildParamsMap(request);
        log.info("easyAr功能获取token");
        JSONObject oj = easyArService.list(params);
        log.info("easyAr功能获取token，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

}
