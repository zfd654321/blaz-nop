package com.bl.nop.wxs.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.nis.api.PhotoService;
import com.bl.nop.wxs.controller.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photo")
public class PhotoController extends BaseController {

    @Autowired
    private PhotoService photoService;


        /**
     * 获取首页信息
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/indexData")
    public void indexData(HttpServletResponse response, HttpServletRequest request) {
        log.info("获取首页信息");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = photoService.indexData(params);
        log.info("获取首页信息，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }


    /**
     * 绑定用户照片
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/bondPhoto")
    public void bondPhoto(HttpServletResponse response, HttpServletRequest request) {
        log.info("绑定用户照片");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = photoService.bondPhoto(params);
        log.info("绑定用户照片，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 用户照片列表
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/photoList")
    public void photoList(HttpServletResponse response, HttpServletRequest request) {
        log.info("用户照片列表");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = photoService.photoList(params);
        log.info("用户照片列表，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 用户排行榜列表
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/rankList")
    public void rankList(HttpServletResponse response, HttpServletRequest request) {
        log.info("用户排行榜列表");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = photoService.rankList(params);
        log.info("用户排行榜列表，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    /**
     * 排行榜详情
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/rankInfo")
    public void rankInfo(HttpServletResponse response, HttpServletRequest request) {
        log.info("排行榜详情");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = photoService.rankInfo(params);
        log.info("排行榜详情，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

}
