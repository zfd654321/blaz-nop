package com.bl.nop.oms.controller.merchant;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.MerchantService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.controller.common.JsonBaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/merchant")
public class MerchantController  extends JsonBaseController {
    @Autowired
	private MerchantService merchantService;

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.merchantService.queryByPage(params);
		return resResultBean;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.merchantService.save(params);
		return resResultBean;
	}

	@RequestMapping(value = "/loadPower", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean loadPower(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.merchantService.loadPower(params);
		return resResultBean;
	}

	@RequestMapping(value = "/deviceSave", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean deviceSave(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.merchantService.deviceSave(params);
		return resResultBean;
	}

	@RequestMapping(value = "/gameSave", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean gameSave(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.merchantService.gameSave(params);
		return resResultBean;
	}

	@RequestMapping(value = "/advertSave", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean advertSave(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.merchantService.advertSave(params);
		return resResultBean;
	}

	@RequestMapping(value = "/poolSave", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean poolSave(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.merchantService.poolSave(params);
		return resResultBean;
	}

	
	

	
}
