package com.bl.nop.nis.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.common.util.JSONUtils;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.game.GameDao;
import com.bl.nop.dao.wx.PhotoGroupDao;
import com.bl.nop.dao.wx.WxRankDao;
import com.bl.nop.entity.game.Game;
import com.bl.nop.entity.wx.PhotoGroup;
import com.bl.nop.entity.wx.WxRank;
import com.bl.nop.nis.api.PhotoService;
import com.bl.nop.nis.dao.PhotoDao;
import com.bl.nop.nis.dto.PhotoDto;
import com.bl.nop.nis.dto.RankDto;
import com.bl.nop.nis.dto.RankInfoDto;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("photoService")
public class PhotoServiceImpl implements PhotoService {

	private final static Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);
	private final static String ERROR_CODE = "10";

	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private PhotoGroupDao photoGroupDao;
	@Autowired
	private GameDao gameDao;
	@Autowired
	private WxRankDao wxRankDao;


	
	@Override
	public JSONObject indexData(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		params.put("type", 1);
		List<PhotoDto> photos = this.photoDao.getPhotoList(params);
		List<RankDto> ranks = this.photoDao.getRankList(params);
		if(!photos.isEmpty()){
			dataContent.put("photo", photos.get(0));
		}
		if(!ranks.isEmpty()){
			dataContent.put("rank", ranks.get(0));
		}
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject bondPhoto(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		String group = StringUtil.toStr(params.get("group"));
		String openId = StringUtil.toStr(params.get("openId"));
		log.info("绑定相册数据，group["+group+"],openId:["+openId+"]");
		//保存相册信息
		PhotoGroup photoGroup = this.photoGroupDao.selectByPrimaryKey(group);
		if (photoGroup == null) {
			return JSONUtils.error(ERROR_CODE + "011", dataContent, "相册数据获取失败");
		}
		else if (!StringUtils.isBlank(photoGroup.getOpenId())) {
			return JSONUtils.error(ERROR_CODE + "012", dataContent, "相册数据已被获取，无法重复获取");
		}
		photoGroup.setOpenId(openId);
		this.photoGroupDao.updateByPrimaryKey(photoGroup);

		//保存排行榜信息
		String gameId = photoGroup.getGameId();
		Game game = this.gameDao.selectByPrimaryKey(gameId);
		if (game.getRankType() == 1 && photoGroup.getScore() > 0) {//该游戏开启排行榜开关，且该次得分>0
			Map<String,Object> queryParam=new HashMap<>();
			queryParam.put("openId", openId);
			queryParam.put("gameId", gameId);
			Date now=new Date();
			WxRank wxRank=this.photoDao.getWxRankById(queryParam);
			if(wxRank==null){//首次计入排行榜
				wxRank=new WxRank();
				wxRank.setGameId(gameId);
				wxRank.setOpenId(openId);
				wxRank.setScore(photoGroup.getScore());
				wxRank.setCreatedAt(now);
				this.wxRankDao.insert(wxRank);
			}else if(wxRank.getScore()<photoGroup.getScore()){//大于之前保留的最高分
				wxRank.setScore(photoGroup.getScore());
				wxRank.setCreatedAt(now);
				this.wxRankDao.updateByPrimaryKey(wxRank);
			}
		}

		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject photoList(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		List<PhotoDto> list = this.photoDao.getPhotoList(params);
		dataContent.put("list", list);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject rankList(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		List<RankDto> list = this.photoDao.getRankList(params);
		dataContent.put("list", list);
		return JSONUtils.success(dataContent);
	}

	@Override
	public JSONObject rankInfo(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		RankDto game = this.photoDao.getRankGame(params);
		List<RankInfoDto> list = this.photoDao.getRankInfoList(params);
		dataContent.put("game", game);
		dataContent.put("list", list);
		return JSONUtils.success(dataContent);
	}


}
