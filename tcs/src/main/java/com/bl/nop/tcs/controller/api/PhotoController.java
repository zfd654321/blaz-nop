package com.bl.nop.tcs.controller.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.PhotoService;
import com.bl.nop.tcs.controller.common.BaseController;
import com.bl.nop.tcs.util.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/photo")
public class PhotoController extends BaseController {
    @Autowired
    private PhotoService photoService;

    /**
     * 上传拍照照片
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/uploadPhoto")
    public void uploadPhoto(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]上传拍照照片");
        Map<String, Object> params = buildParamsMap(request);

        try {
			MultipartHttpServletRequest multipartRequest = null;
			multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multipartRequest.getFiles("file");
			if (files != null && files.size() > 0) {
				MultipartFile file = files.get(0);
				params=FileUtil.saveUploadPhoto(file,params);
			}
		} catch (Exception e) {
			e.printStackTrace();
            JSONObject oj = new JSONObject();
            oj.put("returnCode", "15011");
            oj.put("errorMsg", "文件不存在或格式有误");
            writeResponseByJson(request, response, oj);
            return;
		}

        JSONObject oj = photoService.uploadPhoto(params);
        log.info("[" + pcId + "]上传拍照照片，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    @RequestMapping(value = "/uploadVideo")
    public void uploadVideo(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]上传录屏文件");
        Map<String, Object> params = buildParamsMap(request);

        try {
			MultipartHttpServletRequest multipartRequest = null;
			multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multipartRequest.getFiles("file");
			if (files != null && files.size() > 0) {
				MultipartFile file = files.get(0);
				params=FileUtil.saveUploadVideo(file,params);
			}
		} catch (Exception e) {
			e.printStackTrace();
            JSONObject oj = new JSONObject();
            oj.put("returnCode", "15011");
            oj.put("errorMsg", "文件不存在或格式有误");
            writeResponseByJson(request, response, oj);
            return;
		}

        JSONObject oj = photoService.uploadPhoto(params);
        log.info("[" + pcId + "]上传录屏文件，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

    @RequestMapping(value = "/getQrCode")
    public void getQrCode(HttpServletRequest request, HttpServletResponse response) {
        String pcId = request.getParameter("pcId");
        log.info("[" + pcId + "]获取取照二维码");
        Map<String, Object> params = buildParamsMap(request);
        JSONObject oj = photoService.getQrCode(params);
        log.info("[" + pcId + "]获取取照二维码，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }

   
}
