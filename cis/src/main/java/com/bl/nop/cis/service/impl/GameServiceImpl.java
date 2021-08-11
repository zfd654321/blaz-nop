package com.bl.nop.cis.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.ResourcesService;
import com.bl.nop.cis.dao.OmsResourcesDao;
import com.bl.nop.cis.util.PropertyUtil;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.resources.ResourcesDao;
import com.bl.nop.entity.resources.Resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gameService")
public class GameServiceImpl implements ResourcesService {

	private final static Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

	@Autowired
	private ResourcesDao resourcesDao;
	@Autowired
	private OmsResourcesDao omsResourcesDao;

	// private static final String ERROR_CODE = "041";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<Resources> page = new Page<Resources>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsResourcesDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<Resources> list = this.omsResourcesDao.findItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		String name = StringUtil.toStr(params.get("name"));
		String type = StringUtil.toStr(params.get("type"));
		String ext = StringUtil.toStr(params.get("ext"));
		String md5 = StringUtil.toStr(params.get("md5"));
		String path = StringUtil.toStr(params.get("path"));
		String url = StringUtil.toStr(params.get("url"));
		Integer size = NumberUtil.toInt(params.get("size"));
		String thumbnail = StringUtil.toStr(params.get("thumbnail"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		log.info("新增用户资源>>>>>" + type + ">>>>>" + name);
		Date now = new Date();

		Resources resource = new Resources();
		resource.setName(name);
		resource.setType(type);
		resource.setExt(ext);
		resource.setMd5(md5);
		resource.setPath(path);
		resource.setUrl(url);
		resource.setStatus(1);
		resource.setSize(size);
		resource.setThumbnail(thumbnail);
		resource.setCreatedAt(now);
		resource.setCreatedBy(createdBy);
		resource.setUpdatedAt(now);
		resource.setUpdatedBy(createdBy);
		this.resourcesDao.insert(resource);

		return ResResultBean.success();

	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		Integer id=NumberUtil.toInt(params.get("id"));
		Resources resources=this.resourcesDao.selectByPrimaryKey(id);
		String path=resources.getPath();
		String thumbnail=resources.getThumbnail().replace(PropertyUtil.getProperty("fileUrl"), PropertyUtil.getProperty("filePath"));
		File file=new File(path);
		if(file.exists()){
			file.delete();
		}
		File thumbnailFile=new File(thumbnail);
		if(thumbnailFile.exists()){
			thumbnailFile.delete();
		}
		this.resourcesDao.deleteByPrimaryKey(id);
		return ResResultBean.success();
	}

}
