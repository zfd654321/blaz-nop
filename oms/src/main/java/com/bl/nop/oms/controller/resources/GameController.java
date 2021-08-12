package com.bl.nop.oms.controller.resources;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bl.nop.cis.api.GameService;
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
@RequestMapping("/game")
public class GameController extends JsonBaseController {

	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean list(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.gameService.queryByPage(params);
		return resResultBean;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean save(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		try {
			MultipartHttpServletRequest multipartRequest = null;
			multipartRequest = (MultipartHttpServletRequest) request;
			String id = StringUtil.toStr(params.get("id"));
			String version = StringUtil.toStr(params.get("version"));
			String pathDir = PropertyUtil.getProperty("filePath") + "forever/game/";
			File folder =new File(pathDir + id + "/" + version);
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdirs();
				System.out.println("创建文件夹");
			} else {
				return ResResultBean.error("041003","版本号重复，请更换版本号");
			}
			List<MultipartFile> jsonFiles = multipartRequest.getFiles("jsonFile");
			if (jsonFiles != null && jsonFiles.size() > 0) {
				MultipartFile jsonFile = jsonFiles.get(0);
				String path = pathDir + id + "/" + version + "/game.json";
				FileCopyUtils.copy(jsonFile.getBytes(), new File(path));
			}
			List<MultipartFile> gameFiles = multipartRequest.getFiles("zipFile");
			if (gameFiles != null && gameFiles.size() > 0) {
				MultipartFile gameFile = gameFiles.get(0);
				String path = pathDir + id + "/" + version + "/game.zip";
				FileCopyUtils.copy(gameFile.getBytes(), new File(path));
				FileUtil.unZipIt(path,  pathDir + id + "/" + version + "/gameFile/");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SysUser user = this.getUser(request);
		params.put("createdBy", user.getId());
		ResResultBean resResultBean = this.gameService.save(params);
		return resResultBean;
	}

	@RequestMapping(value = "/version", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean version(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.gameService.version(params);
		return resResultBean;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResResultBean delete(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = this.getParameterMap(request);
		ResResultBean resResultBean = this.gameService.delete(params);
		return resResultBean;
	}

}
