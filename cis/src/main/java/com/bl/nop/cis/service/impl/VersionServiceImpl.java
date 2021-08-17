package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.VersionService;
import com.bl.nop.cis.dao.OmsVersionDao;
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
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Version reitem = this.versionDao.selectByPrimaryKey(id);

		if (reitem != null) {
			return ResResultBean.error(ERROR_CODE + "002", "该版本号已存在");
		}
		log.info("新增版本>>>>>version:" + id);
		Version item = new Version();
		item.setId(id);
		item.setCreatedAt(now);
		item.setCreatedBy(createdBy);
		this.versionDao.insert(item);

		return ResResultBean.success();
	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String id = StringUtil.toStr(params.get("id"));
		Version reitem = this.versionDao.selectByPrimaryKey(id);
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
