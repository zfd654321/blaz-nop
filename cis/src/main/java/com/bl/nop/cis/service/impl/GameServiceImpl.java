package com.bl.nop.cis.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bl.nop.cis.api.GameService;
import com.bl.nop.cis.dao.OmsGameDao;
import com.bl.nop.cis.util.FileUtil;
import com.bl.nop.cis.util.PropertyUtil;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.game.GameDao;
import com.bl.nop.dao.game.GameNetresDao;
import com.bl.nop.dao.game.GameVersionDao;
import com.bl.nop.entity.game.Game;
import com.bl.nop.entity.game.GameNetres;
import com.bl.nop.entity.game.GameVersion;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gameService")
public class GameServiceImpl implements GameService {

	private final static Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

	@Autowired
	private GameDao gameDao;
	@Autowired
	private GameNetresDao gameNetresDao;
	@Autowired
	private GameVersionDao gameVersionDao;
	@Autowired
	private OmsGameDao omsGameDao;

	private static final String ERROR_CODE = "041";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<Game> page = new Page<Game>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsGameDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<Game> list = this.omsGameDao.findItemByPage(param);
		page.setList(list);

		return ResResultBean.success(page);
	}

	@Override
	public ResResultBean save(Map<String, Object> params) {
		if (null == params || params.isEmpty()) {
			return ResResultBean.error(ERROR_CODE + "001", "参数为空");
		}
		String id = StringUtil.toStr(params.get("id"));
		String version = StringUtil.toStr(params.get("version"));
		String gameDir = PropertyUtil.getProperty("filePath") + "forever/game/" + id + "/" + version;
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		boolean edit = StringUtil.toStr(params.get("edit")).equals("true");
		Game game = this.gameDao.selectByPrimaryKey(id);
		Date now = new Date();
		if (edit) {// 游戏更新
			// 校验版本号是否重复
			GameVersion checkVersion = this.omsGameDao.getGameVersion(params);
			if (checkVersion != null) {
				return ResResultBean.error(ERROR_CODE + "003", "版本号已存在，请更改版本号");
			}
		} else {// 游戏新增
			// 校验游戏编号是否重复
			if (game != null) {
				return ResResultBean.error(ERROR_CODE + "002", "游戏编号已存在，请更改游戏编号");
			}
			game = new Game();
			log.info("新增游戏>>>>>" + id + ">>>>>" + id);
		}
		String jsonUrl = gameDir + "/game.json";
		JSONObject game_json = FileUtil.getFileJson(jsonUrl);
		String jsonId = game_json.getString("GameId");
		if (!id.equals(jsonId)) {
			return ResResultBean.error(ERROR_CODE + "002", "游戏编号与json文件中的不符，请检测游戏编号");
		}
		String name = game_json.getString("GameName");
		Integer screen = NumberUtil.toInt(game_json.getString("ScreenType"));
		Integer rankType = NumberUtil.toInt(game_json.getString("RankType"));
		String camera = game_json.getString("CameraType");
		String path = game_json.getString("GamePath");
		String img = PropertyUtil.getProperty("fileUrl") + "forever/game/" + id + "/" + version + "/gameFile/cover.png";
		// 新增游戏对象
		game.setId(id);
		game.setName(name);
		game.setVersion(version);
		game.setScreen(screen);
		game.setRankType(rankType);
		game.setImg(img);
		game.setCamera(camera);
		game.setPath(path);
		game.setUpdatedAt(now);
		game.setUpdatedBy(createdBy);

		if (edit) {// 游戏更新
			this.gameDao.updateByPrimaryKey(game);
			log.info("更新游戏>>>>>" + id + ">>>>>" + version);
		} else {// 游戏新增
			game.setCreatedAt(now);
			game.setCreatedBy(createdBy);
			this.gameDao.insert(game);
			log.info("新增游戏>>>>>" + id + ">>>>>" + version);
		}

		// 新增游戏属性
		this.omsGameDao.cleanGameBetres(id);
		JSONArray proarray = game_json.getJSONArray("ListPropertys");
		if (!proarray.isEmpty()) {
			for (Object listob : proarray) {
				GameNetres netres = new GameNetres();
				JSONObject projson = JSONObject.parseObject(StringUtil.toStr(listob));
				String netresName = projson.getString("name");
				String netresProperty = projson.getString("property");
				String type = projson.getString("type");
				String defaulturl = projson.getString("defaultUrl");
				netres.setGameId(id);
				netres.setName(netresName);
				netres.setProperty(netresProperty);
				netres.setType(type);
				if (!StringUtils.isBlank(defaulturl)) {
					defaulturl=PropertyUtil.getProperty("fileUrl")+defaulturl;
					netres.setDefaulturl(defaulturl);
				}
				this.gameNetresDao.insert(netres);
			}
		}

		// 新增游戏日志
		String versionLog = StringUtil.toStr(params.get("versionLog"));
		GameVersion gameVersion = new GameVersion();
		gameVersion.setGameId(id);
		gameVersion.setStatus(1);
		gameVersion.setVersion(version);
		gameVersion.setVersionLog(versionLog);
		gameVersion.setUpdatedAt(now);
		gameVersion.setUpdatedBy(createdBy);
		this.gameVersionDao.insert(gameVersion);

		// 删除过期版本文件
		List<GameVersion> list = this.omsGameDao.queryOutGameVersion(id);
		if (list != null && list.size() > 0) {
			for (GameVersion versionVo : list) {
				log.info("清理游戏版本>>>>>" + id + ">>>>>" + versionVo.getVersion());
				String repath = PropertyUtil.getProperty("filePath") + "forever/game/" + id + "/"
						+ versionVo.getVersion();
				File refile = new File(repath);
				FileUtil.deleteFile(refile);
				versionVo.setStatus(0);
				this.gameVersionDao.updateByPrimaryKey(versionVo);
			}
		}

		// 生成游戏文件列表json
		FileWriter fwriter = null;
		try {
			JSONArray array = new JSONArray();
			array = FileUtil.traverseFoloder(array, gameDir + "/gameFile", gameDir + "/gameFile");

			JSONArray saveArray = new JSONArray();
			for (Object object : array) {
				JSONObject oj = JSONObject.parseObject(StringUtil.toStr(object));
				String url = PropertyUtil.getProperty("fileUrl") + "forever/game/" + id + "/" + version + "/gameFile"
						+ oj.getString("path");
				oj.put("url", url);
				saveArray.add(oj);
			}
			JSONObject filejson = new JSONObject();
			filejson.put("id", id);
			filejson.put("fileList", saveArray);
			String jsonStr = filejson.toJSONString();
			fwriter = new FileWriter(gameDir + "/file.json");
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
		// 清除zip包
		File zipfile = new File(gameDir + "/game.zip");
		FileUtil.deleteFile(zipfile);

		return ResResultBean.success();

	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String id = StringUtil.toStr(params.get("id"));
		this.gameDao.deleteByPrimaryKey(id);
		this.omsGameDao.cleanGameBetres(id);
		return ResResultBean.success();
	}

	@Override
	public ResResultBean version(Map<String, Object> params) {
		String id = StringUtil.toStr(params.get("id"));
		List<GameVersion> list = this.omsGameDao.queryGameVersionList(id);
		return ResResultBean.success(list);
	}

}
