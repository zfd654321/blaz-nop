package com.bl.nop.tcs.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DownloadService;
import com.bl.nop.tcs.controller.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/download")
public class DownloadController extends BaseController {
    @Autowired
    private DownloadService downloadService;

    /**
     * 获取配置request列表
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/version")
    public void version(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备接口列表");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = downloadService.version(params);
        log.info("[" + pcId + "]获取设备接口列表，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备配置
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/game")
    public void game(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备配置");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = downloadService.game(params);
        log.info("[" + pcId + "]获取设备配置，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备开关
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/resources")
    public void resources(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备配置");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = downloadService.resources(params);
        log.info("[" + pcId + "]获取设备配置，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

}
