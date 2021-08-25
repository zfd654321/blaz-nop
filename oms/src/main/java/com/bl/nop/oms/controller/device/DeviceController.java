package com.bl.nop.oms.controller.device;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.DeviceService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.controller.common.JsonBaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/device")
public class DeviceController extends JsonBaseController {

	@Autowired
	private DeviceService deviceService;

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.deviceService.queryByPage(params);
		return resResultBean;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.save(params);
		return resResultBean;
	}

	@RequestMapping(value = "/freePcList", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean freePcList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.deviceService.freePcList(params);
		return resResultBean;
	}

	@RequestMapping(value = "/intoHouse", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean intoHouse(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.intoHouse(params);
		return resResultBean;
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean updateStatus(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.updateStatus(params);
		return resResultBean;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean delete(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.delete(params);
		return resResultBean;
	}

	@RequestMapping(value = "/loadDeviceConfig", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean loadDeviceConfig(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.deviceService.loadDeviceConfig(params);
		return resResultBean;
	}

	@RequestMapping(value = "/saveDeviceConfig", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean saveDeviceConfig(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.saveDeviceConfig(params);
		return resResultBean;
	}

	@RequestMapping(value = "/loadDeviceAdvert", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean loadDeviceAdvert(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.deviceService.loadDeviceAdvert(params);
		return resResultBean;
	}

	@RequestMapping(value = "/saveDeviceAdvert", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean saveDeviceAdvert(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.saveDeviceAdvert(params);
		return resResultBean;
	}

	@RequestMapping(value = "/loadDeviceGame", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean loadDeviceGame(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.deviceService.loadDeviceGame(params);
		return resResultBean;
	}

	@RequestMapping(value = "/loadDeviceGameNetres", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean loadDeviceGameNetres(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.deviceService.loadDeviceGameNetres(params);
		return resResultBean;
	}

	@RequestMapping(value = "/saveDeviceGame", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean saveDeviceGame(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.saveDeviceGame(params);
		return resResultBean;
	}

	@RequestMapping(value = "/deviceCopy", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean deviceCopy(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.deviceService.deviceCopy(params);
		return resResultBean;
	}

	@RequestMapping(value = "/deviceOnlineCheck", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean deviceOnlineCheck(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.deviceService.deviceOnlineCheck(params);
		return resResultBean;
	}

}
