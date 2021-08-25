package com.bl.nop.tcs.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.ApiService;
import com.bl.nop.tcs.controller.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Autowired
    private ApiService apiService;

    /**
     * 通过软件编码得到对应的设备编号的信息 得到设备的激活信息
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getDeviceId")
    public void getDeviceID(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备激活信息");
        JSONObject oj = apiService.getOnlineDeviceByPcId(pcId);
        log.info("[" + pcId + "]获取设备激活信息，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取最新下载器
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getDownloader")
    public void getDownloader(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取最新下载器信息");
        JSONObject oj = apiService.getDownLoader();
        log.info("[" + pcId + "]获取最新下载器信息，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 获取设备授权日期
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getDeviceOutDate")
    public void getDeviceOutDate(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取设备授权日期");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = apiService.getDeviceOutDate(params);
        log.info("[" + pcId + "]获取设备授权日期，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }
}
