package com.bl.nop.oms.controller.sys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.SysUserService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.controller.common.JsonBaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController extends JsonBaseController {

	@Autowired
	private SysUserService userService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.userService.queryByPage(params);
		return resResultBean;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.userService.save(params);
		return resResultBean;
	}

	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean getById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("userId") String userId) {
		ResResultBean resResultBean = this.userService.getByUserId(userId);
		return resResultBean;
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean del(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.userService.delete(params);
		return resResultBean;
	}
}
