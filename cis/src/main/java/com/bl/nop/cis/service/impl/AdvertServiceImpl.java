package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.AdvertService;
import com.bl.nop.cis.dao.OmsAdvertDao;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.advert.AdvertDao;
import com.bl.nop.dao.advert.AdvertTypeDao;
import com.bl.nop.entity.advert.Advert;
import com.bl.nop.entity.advert.AdvertType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("advertService")
public class AdvertServiceImpl implements AdvertService {

	private final static Logger log = LoggerFactory.getLogger(AdvertServiceImpl.class);

	@Autowired
	private AdvertDao advertDao;
	@Autowired
	private AdvertTypeDao advertTypeDao;
	@Autowired
	private OmsAdvertDao omsAdvertDao;

	private static final String ERROR_CODE = "021";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<Advert> page = new Page<Advert>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsAdvertDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<Advert> list = this.omsAdvertDao.findItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String id = StringUtil.toStr(params.get("id"));
		boolean edit = StringUtil.toStr(params.get("edit")).equals("true");
		String name = StringUtil.toStr(params.get("name"));
		String remarks = StringUtil.toStr(params.get("remarks"));
		Integer type = NumberUtil.toInt(params.get("type"));
		String resource = StringUtil.toStr(params.get("resource"));
		Integer scount = NumberUtil.toInt(params.get("scount"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		String merchantId = StringUtil.toStr(params.get("merchantId"));
		Date now = new Date();
		Advert reitem = this.advertDao.selectByPrimaryKey(id);

		if (edit) {// 修改
			Advert item = reitem;
			item.setName(name);
			item.setMerchantId(merchantId);
			item.setRemarks(remarks);
			item.setType(type);
			item.setResource(resource);
			item.setScount(scount);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.advertDao.updateByPrimaryKey(item);

		} else {// 新增
			if (reitem != null) {
				return ResResultBean.error(ERROR_CODE + "002", "该广告码已存在");
			}
			log.info("新增广告>>>>>id:" + id);
			Advert item = new Advert();
			item.setId(id);
			item.setName(name);
			item.setMerchantId(merchantId);
			item.setRemarks(remarks);
			item.setType(type);
			item.setResource(resource);
			item.setScount(scount);
			item.setCreatedAt(now);
			item.setCreatedBy(createdBy);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.advertDao.insert(item);
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String id = StringUtil.toStr(params.get("id"));
		this.advertDao.deleteByPrimaryKey(id);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean queryTypeByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<AdvertType> page = new Page<AdvertType>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsAdvertDao.findTypeItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<AdvertType> list = this.omsAdvertDao.findTypeItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean saveType(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		Integer id = NumberUtil.toInt(params.get("id"));
		boolean edit = StringUtil.toStr(params.get("edit")).equals("true");
		String name = StringUtil.toStr(params.get("name"));
		String remarks = StringUtil.toStr(params.get("remarks"));
		Integer screen = NumberUtil.toInt(params.get("screen"));
		String bundleName = StringUtil.toStr(params.get("bundleName"));
		String assetName = StringUtil.toStr(params.get("assetName"));
		Integer time = NumberUtil.toInt(params.get("time"));
		Integer type = NumberUtil.toInt(params.get("type"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		AdvertType reitem = this.advertTypeDao.selectByPrimaryKey(id);

		if (edit) {// 修改
			AdvertType item = reitem;
			item.setName(name);
			item.setRemarks(remarks);
			item.setScreen(screen);
			item.setType(type);
			item.setBundleName(bundleName);
			item.setAssetName(assetName);
			item.setTime(time);
			item.setType(type);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.advertTypeDao.updateByPrimaryKey(item);

		} else {// 新增
			if (reitem != null) {
				return ResResultBean.error(ERROR_CODE + "002", "该机器码已存在");
			}
			log.info("新增PC>>>>>pc_id:" + id);
			AdvertType item = new AdvertType();
			item.setName(name);
			item.setRemarks(remarks);
			item.setScreen(screen);
			item.setType(type);
			item.setBundleName(bundleName);
			item.setAssetName(assetName);
			item.setTime(time);
			item.setType(type);
			item.setCreatedAt(now);
			item.setCreatedBy(createdBy);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.advertTypeDao.insert(item);
		}
		return ResResultBean.success();
	}

}
