package com.bl.nop.nis.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.wx.EasyarConfigDao;
import com.bl.nop.dao.wx.EasyarLogDao;
import com.bl.nop.entity.wx.EasyarConfig;
import com.bl.nop.entity.wx.EasyarLog;
import com.bl.nop.nis.api.EasyArService;
import com.bl.nop.nis.dao.EasyarDao;
import com.bl.nop.nis.util.easyar.Auth;
import com.bl.nop.nis.util.easyar.Token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("easyArService")
public class EasyArServiceImpl implements EasyArService {
	private final static Logger log = LoggerFactory.getLogger(DomainServiceImpl.class);
	@Autowired
	private EasyarConfigDao easyarConfigDao;
	@Autowired
	private EasyarLogDao easyarLogDao;
	@Autowired
	private EasyarDao easyarDao;

	@Override
	public JSONObject token() {
		JSONObject dataContent = new JSONObject();
		EasyarConfig easyarConfig = this.easyarConfigDao.selectByPrimaryKey(1);
		Date now = new Date();
		log.info("overTime:" + easyarConfig.getOvertime());
		if (now.after(easyarConfig.getOvertime())) {
			try {
				log.info("ApiKey:" + easyarConfig.getApiKey());
				log.info("ApiSecret:" + easyarConfig.getApiSecret());
				Auth accessInfo = new Auth(easyarConfig.getApiKey(), easyarConfig.getApiSecret());
				String tokenJsonStr = new Token().token(accessInfo);
				log.info("tokenJson:" + tokenJsonStr);
				JSONObject tokenJson = JSONObject.parseObject(tokenJsonStr);
				easyarConfig.setToken(tokenJson.getString("token"));
				easyarConfig.setUpdatedAt(now);
				easyarConfig.setOvertime(DateUtil.addDay(now, 1));
				this.easyarConfigDao.updateByPrimaryKey(easyarConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		dataContent.put("aliKey", easyarConfig.getApiKey());
		dataContent.put("crsAppId", easyarConfig.getCloudKey());
		dataContent.put("expiration", now);
		dataContent.put("expries", 86400);
		dataContent.put("token", easyarConfig.getToken());
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject savelog(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		String name = StringUtil.toStr(params.get("name"));
		String targetId = StringUtil.toStr(params.get("targetId"));
		String type = StringUtil.toStr(params.get("type"));
		Integer status = NumberUtil.toInt(params.get("status"));
		String clientIp = StringUtil.toStr(params.get("request_ip"));
		Date statsTime = new Date();
		EasyarLog easyarLog = new EasyarLog();
		easyarLog.setType(type);
		easyarLog.setStatus(status);
		easyarLog.setTargetId(targetId);
		easyarLog.setName(name);
		easyarLog.setClientIp(clientIp);
		easyarLog.setStatsTime(statsTime);
		this.easyarLogDao.insert(easyarLog);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject list(Map<String, Object> param) {
		
		JSONObject dataContent = new JSONObject();
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<EasyarLog> page = new Page<EasyarLog>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			dataContent.put("data", page);
			return dataContent;
		}

		int count = this.easyarDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<EasyarLog> list = this.easyarDao.findItemByPage(param);
		page.setList(list);
		dataContent.put("data", page);
		return dataContent;
	}

	public static void main(String[] args) {
		System.out.println(new Date().toString());
	}

}
