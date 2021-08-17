package com.bl.nop.tcs.controller.device;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.tcs.controller.common.BaseController;
import com.bl.nop.tcs.service.device.DeviceBaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ApiController extends BaseController {
    @Autowired
    private DeviceBaseService deviceBaseService;

    /**
     * U3D前端通过软件编码得到对应的设备编号的信息 得到设备的激活信息
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getDeviceId")
    public void getDeviceID(HttpServletRequest request, HttpServletResponse response) {
        String sId = request.getParameter("s_id");
        log.info("请求根据设备软件码[" + sId + "]获取设备激活信息");
        JSONObject oj = deviceBaseService.getDeviceID(sId);
        log.info("请求根据设备软件码[" + sId + "]获取设备激活信息，返回数据：" + oj.toJSONString());
        writeResponseByJson(request, response, oj);
    }
}
