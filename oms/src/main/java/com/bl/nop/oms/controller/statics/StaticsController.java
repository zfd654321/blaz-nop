package com.bl.nop.oms.controller.statics;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.DataService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.oms.controller.common.JsonBaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/static")
public class StaticsController  extends JsonBaseController {
    @Autowired
	private DataService dataService;

	@RequestMapping(value = "/showData", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean showData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.dataService.showData(params);
		return resResultBean;
	}
}
