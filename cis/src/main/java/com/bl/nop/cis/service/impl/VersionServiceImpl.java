package com.bl.nop.cis.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bl.nop.cis.api.VersionService;
import com.bl.nop.cis.dao.OmsVersionDao;
import com.bl.nop.cis.util.FileUtil;
import com.bl.nop.cis.util.PropertyUtil;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.version.VersionDao;
import com.bl.nop.dao.version.VersionDownloaderDao;
import com.bl.nop.entity.version.Version;
import com.bl.nop.entity.version.VersionDownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("versionService")
public class VersionServiceImpl implements VersionService {

	private final static Logger log = LoggerFactory.getLogger(VersionServiceImpl.class);

	@Autowired
	private VersionDao versionDao;
	@Autowired
	private VersionDownloaderDao versionDownloaderDao;
	@Autowired
	private OmsVersionDao omsVersionDao;

	private static final String ERROR_CODE = "021";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<Version> page = new Page<Version>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsVersionDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<Version> list = this.omsVersionDao.findItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String id = StringUtil.toStr(params.get("id"));
		Integer screen = NumberUtil.toInt(params.get("screen"));
		String versionLog = StringUtil.toStr(params.get("versionLog"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Version reitem = this.versionDao.selectByPrimaryKey(id);

		if (reitem != null) {
			return ResResultBean.error(ERROR_CODE + "002", "该版本号已存在");
		}
		log.info("新增版本>>>>>version:" + id);
		String filePath = PropertyUtil.getProperty("filePath") + "forever/version/procedure/" + id + "/";
		String fileUrl = PropertyUtil.getProperty("fileUrl") + "forever/version/procedure/" + id + "/";
		File file=new File(filePath);
		if(!file.exists()){
			return ResResultBean.error(ERROR_CODE + "003", "未找到版本文件");
		}
		Version item = new Version();
		item.setId(id);
		item.setFilePath(filePath);
		item.setScreen(screen);
		item.setStatus(1);
		item.setVersionLog(versionLog);
		item.setCreatedAt(now);
		item.setCreatedBy(createdBy);
		this.versionDao.insert(item);

		// 添加版本文件列表
		FileWriter fwriter = null;
		try {
			JSONArray array = new JSONArray();
			array = FileUtil.traverseFoloder(array, filePath, filePath);

			JSONArray saveArray = new JSONArray();
			for (Object object : array) {
				JSONObject oj = JSONObject.parseObject(StringUtil.toStr(object));
				String url = fileUrl + oj.getString("path");
				oj.put("url", url);
				saveArray.add(oj);
			}
			JSONObject filejson = new JSONObject();
			filejson.put("id", id);
			filejson.put("fileList", saveArray);
			String jsonStr = filejson.toJSONString();
			fwriter = new FileWriter(filePath + "file.json");
			fwriter.write(jsonStr);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return ResResultBean.success();
	}

	@Override
	public ResResultBean build(Map<String, Object> params) {
		String lastVersion = StringUtil.toStr(params.get("lastVersion"));
		String newVersion = StringUtil.toStr(params.get("newVersion"));
		String addVersion = "new_" + lastVersion + "_" + newVersion;
		Version oldVersion = this.versionDao.selectByPrimaryKey(lastVersion);
		if (oldVersion == null) {
			return ResResultBean.error(ERROR_CODE + "011", "指定的旧版本文件不存在");
		}
		Version Version = this.versionDao.selectByPrimaryKey(newVersion);
		if (Version != null) {
			return ResResultBean.error(ERROR_CODE + "010", "该版本号已上传，请勿重复操作");
		}

		String lastPath = PropertyUtil.getProperty("filePath") + "forever/version/procedure/" + lastVersion;
		String newPath = PropertyUtil.getProperty("filePath") + "forever/version/procedure/" + newVersion;
		String addPath = PropertyUtil.getProperty("filePath") + "forever/version/incremental/" + addVersion;

		File lastDir = new File(lastPath);
		File addDir = new File(addPath);
		if (!lastDir.exists()) {
			return ResResultBean.error(ERROR_CODE + "011", "指定的旧版本文件不存在");
		}
		if (!addDir.exists()) {
			return ResResultBean.error(ERROR_CODE + "012", "增量包文件未上传,请检查路径：" + addPath);
		}

		// 拷贝旧版本文件
		FileUtil.copyFolder(lastPath, newPath, "");
		// 拷贝增量包文件
		FileUtil.copyFolder(addDir + "/" + newVersion, newPath, "");

		return ResResultBean.success();
	}

	@Override
	public ResResultBean check(Map<String, Object> params) {
		String lastVersion = StringUtil.toStr(params.get("lastVersion"));
		String newVersion = StringUtil.toStr(params.get("newVersion"));
		String addVersion = "new_" + lastVersion + "_" + newVersion;

		String newPath = PropertyUtil.getProperty("filePath") + "forever/version/procedure/" + newVersion;
		String newUrl = PropertyUtil.getProperty("fileUrl") + "forever/version/procedure/" + newVersion;
		String addPath = PropertyUtil.getProperty("filePath") + "forever/version/incremental/" + addVersion;

		String vjson = addPath + "/Version.json";
		JSONObject version_json = FileUtil.getFileJson(vjson);
		String version_id = version_json.getString("VersionNum");
		if (!newVersion.equals(version_id)) {
			return ResResultBean.error(ERROR_CODE + "013", "校验文件版本号与输入的版本号不一致");
		}

		try {
			JSONArray software = version_json.getJSONArray("VersionExeFiles");
			JSONArray fileList = new JSONArray();
			for (int i = 0; i < software.size(); i++) {
				JSONObject filejson = software.getJSONObject(i);
				String filePath = filejson.getString("FilePath");
				String fileMd5 = filejson.getString("FileMD5");
				File file = new File(newPath + "/" + filePath);
				if (!file.exists() || !fileMd5.equals(FileUtil.getMd5ByFile(file))) {
					return ResResultBean.error(ERROR_CODE + "014", "文件校验失败..." + filePath);
				}
				JSONObject oj = new JSONObject();
				oj.put("name", file.getName());
				oj.put("md5", fileMd5);
				oj.put("path", filePath);
				oj.put("length", file.length());
				oj.put("url", newUrl + "/" + filePath);
				fileList.add(oj);
			}
			JSONObject fileJson = new JSONObject();
			fileJson.put("version", newVersion);
			fileJson.put("fileList", fileList);
			String jsonStr = fileJson.toJSONString();
			FileWriter fwriter = new FileWriter(newPath + "/file.json");
			fwriter.write(jsonStr);
			fwriter.flush();
			fwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResResultBean.success();
	}

	@Override
	public ResResultBean add(Map<String, Object> params) {
		String lastVersion = StringUtil.toStr(params.get("lastVersion"));
		String newVersion = StringUtil.toStr(params.get("newVersion"));
		String versionLog = StringUtil.toStr(params.get("versionLog"));

		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Version oldVersion = this.versionDao.selectByPrimaryKey(lastVersion);
		Version item = new Version();
		item.setId(newVersion);
		item.setFilePath(PropertyUtil.getProperty("filePath") + "forever/version/procedure/" + newVersion);
		item.setScreen(oldVersion.getScreen());
		item.setVersionLog(versionLog);
		item.setStatus(1);
		item.setCreatedAt(now);
		item.setCreatedBy(createdBy);
		this.versionDao.insert(item);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean clean(Map<String, Object> params) {
		String lastVersion = StringUtil.toStr(params.get("lastVersion"));
		String newVersion = StringUtil.toStr(params.get("newVersion"));
		String addVersion = "new_" + lastVersion + "_" + newVersion;
		String addPath = PropertyUtil.getProperty("filePath") + "forever/version/incremental/" + addVersion;
		Version oldVersion = this.versionDao.selectByPrimaryKey(lastVersion);
		Map<String, Object> map = new HashMap<>();
		map.put("screen", oldVersion.getScreen());
		// 清理过期旧版本（保留3个版本）
		List<Version> outlist = this.omsVersionDao.queryOutVersion(map);
		if (!outlist.isEmpty()) {
			for (Version version : outlist) {
				File file = new File(version.getFilePath());
				FileUtil.deleteFile(file);
				version.setStatus(2);
				this.versionDao.updateByPrimaryKey(version);
			}
		}
		// 清理增量包
		File addfile = new File(addPath);
		FileUtil.deleteFile(addfile);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String id = StringUtil.toStr(params.get("id"));
		Version reitem = this.versionDao.selectByPrimaryKey(id);
		File file = new File(reitem.getFilePath());
		FileUtil.deleteFile(file);
		reitem.setStatus(3);
		this.versionDao.updateByPrimaryKey(reitem);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean downLoaderList(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<VersionDownloader> page = new Page<VersionDownloader>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsVersionDao.findDownloaderItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<VersionDownloader> list = this.omsVersionDao.findDownloaderItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean downloaderSave(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String id = StringUtil.toStr(params.get("id"));
		String path = StringUtil.toStr(params.get("path"));
		String url = StringUtil.toStr(params.get("url"));
		String md5 = StringUtil.toStr(params.get("md5"));
		String versionLog = StringUtil.toStr(params.get("versionLog"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		VersionDownloader reitem = this.versionDownloaderDao.selectByPrimaryKey(id);

		if (reitem != null) {
			return ResResultBean.error(ERROR_CODE + "002", "该版本号已存在");
		}
		VersionDownloader item = new VersionDownloader();
		item.setId(id);
		item.setPath(path);
		item.setUrl(url);
		item.setVersionLog(versionLog);
		item.setMd5(md5);
		item.setStatus(1);
		item.setCreatedAt(now);
		item.setCreatedBy(createdBy);
		this.versionDownloaderDao.insert(item);

		return ResResultBean.success();
	}

	@Override
	public ResResultBean downloaderDelete(Map<String, Object> params) {
		String id = StringUtil.toStr(params.get("id"));
		VersionDownloader reitem = this.versionDownloaderDao.selectByPrimaryKey(id);
		reitem.setStatus(0);
		this.versionDownloaderDao.updateByPrimaryKey(reitem);
		return ResResultBean.success();
	}

}
