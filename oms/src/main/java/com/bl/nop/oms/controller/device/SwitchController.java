package com.bl.nop.oms.controller.device;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.DeviceSwitchService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.controller.common.JsonBaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/switch")
public class SwitchController extends JsonBaseController {

	@Autowired
	private DeviceSwitchService switchService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.switchService.queryByPage(params);
		return resResultBean;
	}

	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean getById(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.switchService.getById(params);
		return resResultBean;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.switchService.save(params);
		return resResultBean;
	}
	
}
