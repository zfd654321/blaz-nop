package com.bl.nop.tcs.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DataService;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.tcs.controller.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/data")
public class DataController extends BaseController {
    @Autowired
    private DataService dataService;

    /**
     * 获取服务器时间
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/serverTime")
    public void serverTime(HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataContent = new JSONObject();
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取服务器时间");
        String serverTime=DateUtil.getStringYMDHMS();
        dataContent.put("serverTime", serverTime);
        JSONObject oj = JSONUtils.success(dataContent);
        log.info("[" + pcId + "]获取设备接口列表，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 设备定时心跳数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/heartData")
    public void heartData(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备配置");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = dataService.heartData(params);
        log.info("[" + pcId + "]获取设备配置，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备开关
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/gameData")
    public void gameData(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备配置");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = dataService.gameData(params);
        log.info("[" + pcId + "]获取设备配置，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备广告
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/errorData")
    public void errorData(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备广告");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = dataService.errorData(params);
        log.info("[" + pcId + "]获取设备广告，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

}
