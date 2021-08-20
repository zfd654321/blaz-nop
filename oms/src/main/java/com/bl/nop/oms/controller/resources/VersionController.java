package com.bl.nop.oms.controller.resources;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.VersionService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.entity.sys.SysUser;
import com.bl.nop.oms.controller.common.JsonBaseController;
import com.bl.nop.oms.util.FileUtil;
import com.bl.nop.oms.util.PropertyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/version")
public class VersionController extends JsonBaseController {

	@Autowired
	private VersionService versionService;

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.versionService.queryByPage(params);
		return resResultBean;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.versionService.save(params);
		return resResultBean;
	}
	@RequestMapping(value = "/build", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean build(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.versionService.build(params);
		return resResultBean;
	}
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean check(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.versionService.check(params);
		return resResultBean;
	}
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean add(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.versionService.add(params);
		return resResultBean;
	}
	@RequestMapping(value = "/clean", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean clean(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.versionService.clean(params);
		return resResultBean;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean delete(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.versionService.delete(params);
		return resResultBean;
	}

	@RequestMapping(value = "/downLoaderList", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean downLoaderList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.versionService.downLoaderList(params);
		return resResultBean;
	}

	@RequestMapping(value = "/downloaderSave", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean downloaderSave(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		String id = StringUtil.toStr(params.get("id"));

		try {
			MultipartHttpServletRequest multipartRequest = null;
			multipartRequest = (MultipartHttpServletRequest) request;
			String pathDir = PropertyUtil.getProperty("filePath") + "forever/downloader/" + id + ".zip";
			String urlDir = PropertyUtil.getProperty("fileUrl") + "forever/downloader/" + id + ".zip";
			File zipFile = new File(pathDir);
			if (zipFile.exists()) {
				return ResResultBean.error("041003", "版本号重复，请更换版本号");
			}
			List<MultipartFile> files = multipartRequest.getFiles("file");
			if (files != null && files.size() > 0) {
				MultipartFile file = files.get(0);
				FileCopyUtils.copy(file.getBytes(), zipFile);
				String md5 = FileUtil.getMd5ByFile(zipFile);
				params.put("md5", md5);
				params.put("path", pathDir);
				params.put("url", urlDir);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.versionService.downloaderSave(params);
		return resResultBean;
	}

	@RequestMapping(value = "/downloaderDelete", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean downloaderDelete(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.versionService.downloaderDelete(params);
		return resResultBean;
	}

}
