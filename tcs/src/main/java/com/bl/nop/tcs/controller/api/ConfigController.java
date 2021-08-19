package com.bl.nop.tcs.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.ConfigService;
import com.bl.nop.tcs.controller.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {
    @Autowired
    private ConfigService configService;

    /**
     * 获取配置request列表
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/list")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备接口列表");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = configService.list(params);
        log.info("[" + pcId + "]获取设备接口列表，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备配置
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/deviceConfig")
    public void deviceConfig(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备配置");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = configService.deviceConfig(params);
        log.info("[" + pcId + "]获取设备配置，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备开关
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/deviceSwitch")
    public void deviceSwitch(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备配置");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = configService.deviceSwitch(params);
        log.info("[" + pcId + "]获取设备配置，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备广告
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/advert")
    public void advert(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备广告");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = configService.advert(params);
        log.info("[" + pcId + "]获取设备广告，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备游戏
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/game")
    public void game(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备游戏");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = configService.game(params);
        log.info("[" + pcId + "]获取设备游戏，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备游戏2d资源
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/gameNetres")
    public void gameNetres(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备游戏2d资源");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = configService.gameNetres(params);
        log.info("[" + pcId + "]获取设备游戏2d资源，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

}
