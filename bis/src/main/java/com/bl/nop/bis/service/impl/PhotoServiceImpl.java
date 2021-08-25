package com.bl.nop.bis.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.bis.api.DomainService;
import com.bl.nop.bis.api.PhotoService;
import com.bl.nop.bis.dao.PhotoDao;
import com.bl.nop.bis.dto.GameRankDto;
import com.bl.nop.bis.util.PropertyUtil;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.game.GameDao;
import com.bl.nop.dao.wx.PhotoGroupDao;
import com.bl.nop.dao.wx.PhotoImgDao;
import com.bl.nop.entity.game.Game;
import com.bl.nop.entity.wx.PhotoGroup;
import com.bl.nop.entity.wx.PhotoImg;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("photoService")
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private DomainService domainService;
	@Autowired
	private PhotoGroupDao photoGroupDao;
	@Autowired
	private PhotoImgDao photoImgDao;
	@Autowired
	private GameDao gameDao;
	@Autowired
	private PhotoDao photoDao;



	private final static Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

	private final static String ERROR_CODE = "15";

	@Override
	public JSONObject uploadPhoto(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		String group = StringUtil.toStr(params.get("group"));
		String md5 = StringUtil.toStr(params.get("md5"));
		String url = StringUtil.toStr(params.get("url"));
		Integer size = NumberUtil.toInt(params.get("md5"));
		Date now = new Date();
		if (StringUtils.isBlank(group)) {
			group = deviceId + DateUtil.getStringYMDHMS();
			PhotoGroup photoGroup = new PhotoGroup();
			photoGroup.setId(group);
			photoGroup.setDeviceId(deviceId);
			photoGroup.setCreatedTime(now);
			this.photoGroupDao.insert(photoGroup);
		}else{
			PhotoGroup photoGroup=this.photoGroupDao.selectByPrimaryKey(group);
			if(photoGroup==null){
				return JSONUtils.error(ERROR_CODE + "012", dataContent, "图片组编号出错");
			}
		}
		log.info("上传拍照文件"+url);
		PhotoImg photoImg=new PhotoImg();
		photoImg.setDeviceId(deviceId);
		photoImg.setGroup(group);
		photoImg.setUrl(url);
		photoImg.setSize(size);
		photoImg.setMd5(md5);
		this.photoImgDao.insert(photoImg);

		dataContent.put("group", group);
		dataContent.put("url", url);

		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject getQrCode(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		JSONObject checkoj = this.domainService.handle(params);
		String deviceId = checkoj.getString("deviceId");
		String group = StringUtil.toStr(params.get("group"));
		String gameId = StringUtil.toStr(params.get("gameId"));
		Integer score = NumberUtil.toInt(params.get("score"));
		Date now = new Date();
		PhotoGroup photoGroup = new PhotoGroup();
		if (StringUtils.isBlank(group)) {
			group = deviceId + DateUtil.getStringYMDHMS();
			photoGroup.setId(group);
			photoGroup.setDeviceId(deviceId);
			photoGroup.setCreatedTime(now);
			photoGroup.setGameId(gameId);
			photoGroup.setScore(score);
			this.photoGroupDao.insert(photoGroup);
		}else{
			photoGroup=this.photoGroupDao.selectByPrimaryKey(group);
			if(photoGroup==null){
				return JSONUtils.error(ERROR_CODE + "012", dataContent, "图片组编号出错");
			}
			photoGroup.setGameId(gameId);
			photoGroup.setScore(score);
			this.photoGroupDao.updateByPrimaryKey(photoGroup);
		}
		String qrCodeUrl=MessageFormat.format(PropertyUtil.getProperty("PhotoQrCodeUrl"), group);
		Game game=this.gameDao.selectByPrimaryKey(gameId);
		List<GameRankDto> topList=new ArrayList<>();
		Integer rankNum=0;
		if(game.getRankType()==1){
			Map<String,Object> qparams=new HashMap<>();
			qparams.put("gameId", gameId);
			qparams.put("score", score+"");
			topList=this.photoDao.qeuryTopList(qparams);
			rankNum=this.photoDao.queryGameRank(qparams);

		}
		dataContent.put("qrCodeUrl", qrCodeUrl);
		dataContent.put("rankNum", rankNum);
		dataContent.put("topList", topList);
		return JSONUtils.success(dataContent);
	}

}
