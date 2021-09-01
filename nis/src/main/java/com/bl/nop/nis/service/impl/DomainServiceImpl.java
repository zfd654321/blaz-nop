package com.bl.nop.nis.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bl.nop.common.util.HttpUtil;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.dao.wx.WxUserDao;
import com.bl.nop.entity.wx.WxUser;
import com.bl.nop.nis.api.DomainService;
import com.bl.nop.nis.util.PropertyUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("domainService")
public class DomainServiceImpl implements DomainService {

	private final static Logger log = LoggerFactory.getLogger(DomainServiceImpl.class);
	private final static String ERROR_CODE = "10";

	@Autowired
	private WxUserDao wxUserDao;

	@Override
	public JSONObject getWXUserOpenId(String code) {
		JSONObject dataContent = new JSONObject();
		try {
			log.info("根据CODE[" + code + "]获取小程序openid");
			String OAUTH2_URL = MessageFormat.format(PropertyUtil.getProperty("AuthUrl"),
					PropertyUtil.getProperty("AppID"), PropertyUtil.getProperty("AppSecret"), code);
			byte[] bys = HttpUtil.bodySSL(OAUTH2_URL + code, "");
			String result = new String(bys, "UTF-8");
			log.info("根据CODE[" + code + "]获取小程序openid:" + result);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(result);
			if (jsonNode != null && null == jsonNode.get("errcode")) {
				String openid = jsonNode.path("openid").textValue();
				log.info("根据CODE[" + code + "]获取小程序openid，openid:" + openid);
				WxUser user = this.wxUserDao.selectByPrimaryKey(openid);
				Date now = new Date();
				if (user == null) {
					user = new WxUser();
					user.setOpenId(openid);
					user.setCreatedAt(now);
					user.setStatus(1);
					user.setUpdatedAt(now);
					this.wxUserDao.insert(user);
				} else {
					user.setUpdatedAt(now);
					this.wxUserDao.updateByPrimaryKey(user);
				}
				dataContent.put("openid", openid);
				dataContent.put("user", user);
				return JSONUtils.success(dataContent);
			} else {
				return JSONUtils.error(ERROR_CODE + "011", dataContent, "获取openid请求出错");
			}

		} catch (Exception e) {
			return JSONUtils.error(ERROR_CODE + "012", dataContent, "获取openid请求出错");
		}
	}

	@Override
	public JSONObject userData(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		Date now = new Date();
		WxUser user = JSON.parseObject(JSON.toJSONString(params), WxUser.class);
		user.setUpdatedAt(now);
		user.setStatus(2);
		this.wxUserDao.updateByPrimaryKey(user);
		dataContent.put("user", user);
		return JSONUtils.success(dataContent);
	}

}
