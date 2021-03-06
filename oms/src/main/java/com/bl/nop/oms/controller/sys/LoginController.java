package com.bl.nop.oms.controller.sys;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.PermissionService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.controller.common.JsonBaseController;
import com.bl.nop.oms.util.CommonConst;
import com.bl.nop.oms.util.DrawCodeUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class LoginController extends JsonBaseController {

	@Autowired
	private PermissionService permissionService;

	private static String ERROR_CODE = "201001";

	@RequestMapping("login")
	@ResponseBody
	public ResResultBean login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("rancode") String rancode) {
		if (StringUtils.isBlank(rancode)) {
			return ResResultBean.error(ERROR_CODE + "01001", "验证码为空");
		}

		String code = (String) request.getSession().getAttribute(CommonConst.RANDOMCODE);
		if (!rancode.equalsIgnoreCase(code)) {
			return ResResultBean.error(ERROR_CODE + "01002", "验证码错误");
		}
		ResResultBean resResultBean = permissionService.loginUser(username, password);
		if (resResultBean.isSuccess()) {
			setUser(request, resResultBean.getData());
		}
		return resResultBean;
	}

	@RequestMapping("getMenu")
	@ResponseBody
	public ResResultBean getMenu(HttpServletRequest request, HttpServletResponse response) {
		SysUser user = getUser(request);
		ResResultBean resResultBean = permissionService.getMenuTree(user.getRole());
		return resResultBean;
	}

	@RequestMapping("findRoleMenu")
	@ResponseBody
	public ResResultBean findRoleMenu(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("roleId") String roleId) {
		ResResultBean resResultBean = permissionService.getMenuTree(roleId);
		return resResultBean;
	}

	@RequestMapping("imgcode")
	public void imgcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			response.setContentType("image/jpeg");
			ServletOutputStream sos = response.getOutputStream();

			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			char[] rands = DrawCodeUtil.generateCheckCode();
			request.getSession().setAttribute(CommonConst.RANDOMCODE, new String(rands));
			// 初始化图�?
			BufferedImage image = DrawCodeUtil.draw(rands);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "JPEG", bos);
			byte[] buf = bos.toByteArray();
			response.setContentLength(buf.length);
			sos.write(buf);
			bos.close();
			sos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("getUser")
	@ResponseBody
	public ResResultBean getUser(HttpServletRequest request, HttpServletResponse response) {
		SysUser user = getUser(request);
		if (null != user) {
			return ResResultBean.success(user);
		}
		return ResResultBean.error();
	}

	@RequestMapping("exit")
	@ResponseBody
	public ResResultBean exit(HttpServletRequest request, HttpServletResponse response) {
		clearSession(request);
		return ResResultBean.success();
	}

	@RequestMapping("editPass")
	@ResponseBody
	public ResResultBean editPass(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("newpass") String newpass, @RequestParam("oldpass") String oldpass) {
		SysUser user = getUser(request);
		ResResultBean resResultBean = this.permissionService.updatePass(oldpass, newpass, user.getId());
		return resResultBean;
	}

	@RequestMapping("userList")
	@ResponseBody
	public ResResultBean userList() {
		ResResultBean resResultBean = this.permissionService.getUserList();
		return resResultBean;
	}
}
