package com.bl.nop.oms.controller.resources;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.ResourcesService;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.Md5Util;
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
@RequestMapping("/resources")
public class ResourcesController extends JsonBaseController {

	@Autowired
	private ResourcesService resourcesService;

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.resourcesService.queryByPage(params);
		return resResultBean;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		try {
			MultipartHttpServletRequest multipartRequest = null;
			multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multipartRequest.getFiles("file");
			String type = StringUtil.toStr(params.get("type"));
			if (files != null && files.size() > 0) {
				MultipartFile file = files.get(0);
				Integer size = Integer.valueOf("" + file.getSize());// 文件大小
				String filename = file.getOriginalFilename();
				String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();// 文件后缀
				String fileId = "CMSFile" + DateUtil.getStringYMDHMS();
				String pathDir = PropertyUtil.getProperty("filePath") + "forever/resouces/";
				String urlDir = PropertyUtil.getProperty("fileUrl") + "forever/resouces/";
				String path = pathDir + type + "/" + fileId + extName;
				String url = urlDir + type + "/" + fileId + extName;
				FileCopyUtils.copy(file.getBytes(), new File(path));
				File fileL = new File(path);
				String md5 = Md5Util.getMd5ByFile(fileL);
				String thumbnailpath = pathDir + type + "/" + fileId + "_cut.png";
				String thumbnailurl = urlDir + type + "/" + fileId + "_cut.png";
				params.put("md5", md5);
				params.put("path", path);
				params.put("url", url);
				params.put("size", size);
				params.put("ext", extName);
				switch (type) {
					case "img": {
						FileUtil.getImageResourcesCutImage(path, thumbnailpath);
						break;
					}
					case "video": {
						FileUtil.getVideoResourcesCutImage(path, thumbnailpath);
						break;
					}
					case "other": {
						thumbnailurl = urlDir + "/file_default.jpg";
						break;
					}
				}
				params.put("thumbnail", thumbnailurl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.resourcesService.save(params);
		return resResultBean;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean delete(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.resourcesService.delete(params);
		return resResultBean;
	}

}
