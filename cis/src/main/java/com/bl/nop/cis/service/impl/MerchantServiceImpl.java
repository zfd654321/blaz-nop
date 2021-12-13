package com.bl.nop.cis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.cis.api.MerchantService;
import com.bl.nop.cis.dao.OmsMerchantDao;
import com.bl.nop.cis.dto.MerchantAdvertTypeDto;
import com.bl.nop.cis.dto.MerchantDeviceDto;
import com.bl.nop.cis.dto.MerchantGameDto;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.merchant.MerchantAdvertTypeDao;
import com.bl.nop.dao.merchant.MerchantDao;
import com.bl.nop.dao.merchant.MerchantDeviceDao;
import com.bl.nop.dao.merchant.MerchantGameDao;
import com.bl.nop.dao.merchant.MerchantPoolDao;
import com.bl.nop.entity.merchant.Merchant;
import com.bl.nop.entity.merchant.MerchantAdvertType;
import com.bl.nop.entity.merchant.MerchantDevice;
import com.bl.nop.entity.merchant.MerchantGame;
import com.bl.nop.entity.merchant.MerchantPool;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("merchantService")
public class MerchantServiceImpl implements MerchantService {

	@Autowired
	private OmsMerchantDao omsMerchantDao;
	@Autowired
	private MerchantDao merchantDao;
	@Autowired
	private MerchantPoolDao merchantPoolDao;
	@Autowired
	private MerchantDeviceDao merchantDeviceDao;
	@Autowired
	private MerchantGameDao merchantGameDao;
	@Autowired
	private MerchantAdvertTypeDao merchantAdvertTypeDao;

	private static final String ERROR_CODE = "051";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<Merchant> page = new Page<Merchant>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsMerchantDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<Merchant> list = this.omsMerchantDao.findItemByPage(param);
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
		Integer type = NumberUtil.toInt(params.get("type"));
		String remarks = StringUtil.toStr(params.get("remarks"));
		String username = StringUtil.toStr(params.get("username"));
		String password = StringUtil.toStr(params.get("password"));
		String contacts = StringUtil.toStr(params.get("contacts"));
		String telphone = StringUtil.toStr(params.get("telphone"));
		String email = StringUtil.toStr(params.get("email"));
		String address = StringUtil.toStr(params.get("address"));
		String outDate = StringUtil.toStr(params.get("outDate"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		Merchant reitem = this.merchantDao.selectByPrimaryKey(id);
		if (edit) {// 修改
			Merchant item = reitem;
			if (StringUtils.isNotBlank(password)) {
				item.setPassword(Md5Util.toMd5(password));
			}
			item.setName(name);
			item.setType(type);
			item.setRemarks(remarks);
			item.setUsername(username);
			item.setContacts(contacts);
			item.setTelphone(telphone);
			item.setEmail(email);
			item.setAddress(address);
			item.setOutDate(DateUtil.strToDateShort(outDate));

			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.merchantDao.updateByPrimaryKey(item);

		} else {// 新增
			Merchant item = new Merchant();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String newId = "BL" + format.format(new Date());
			item.setId(newId);
			item.setName(name);
			item.setType(type);
			item.setRemarks(remarks);
			item.setUsername(username);
			item.setPassword(Md5Util.toMd5(password));
			item.setStatus(0);
			item.setContacts(contacts);
			item.setTelphone(telphone);
			item.setEmail(email);
			item.setAddress(address);
			item.setOutDate(DateUtil.strToDateShort(outDate));

			item.setCreatedAt(now);
			item.setCreatedBy(createdBy);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.merchantDao.insert(item);
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean loadPower(Map<String, Object> params) {
		JSONObject result = new JSONObject();
		String merchantId = StringUtil.toStr(params.get("merchantId"));
		List<MerchantDeviceDto> deviceList = this.omsMerchantDao.loadMerchantDevice(params);
		List<String> checkDevices = new ArrayList<>();
		for (MerchantDeviceDto merchantDeviceDto : deviceList) {
			if (merchantId.equals(merchantDeviceDto.getMerchantId())) {
				checkDevices.add(merchantDeviceDto.getDeviceId());
			}
		}
		result.put("deviceList", deviceList);
		result.put("checkDevices", checkDevices);

		List<MerchantGameDto> gameList = this.omsMerchantDao.loadMerchantGame(params);
		List<String> checkGames = new ArrayList<>();
		for (MerchantGameDto merchantGameDto : gameList) {
			if (merchantId.equals(merchantGameDto.getMerchantId())) {
				checkGames.add(merchantGameDto.getId());
			}
		}
		result.put("gameList", gameList);
		result.put("checkGames", checkGames);

		List<MerchantAdvertTypeDto> advertList = this.omsMerchantDao.loadMerchantAdvert(params);
		List<Integer> checkAdverts = new ArrayList<>();
		for (MerchantAdvertTypeDto merchantAdvertTypeDto : advertList) {
			if (merchantId.equals(merchantAdvertTypeDto.getMerchantId())) {
				checkAdverts.add(merchantAdvertTypeDto.getId());
			}
		}
		result.put("advertList", advertList);
		result.put("checkAdverts", checkAdverts);

		MerchantPool pool = merchantPoolDao.selectByPrimaryKey(merchantId);
		if (pool == null) {
			pool = new MerchantPool();
			pool.setId(merchantId);
			pool.setPoolSize(0);
			pool.setUsedSize(0);
			this.merchantPoolDao.insert(pool);
		}
		result.put("merchantPool", pool);

		return ResResultBean.success(result);
	}

	@Override
	public ResResultBean deviceSave(Map<String, Object> params) {
		String merchantId = StringUtil.toStr(params.get("merchantId"));
		String deviceIds = StringUtil.toStr(params.get("deviceIds"));
		this.omsMerchantDao.cleanMerchantDevice(params);
		if (StringUtils.isNotBlank(deviceIds)) {
			String[] deviceIdList = deviceIds.split(",");
			for (String deviceId : deviceIdList) {
				MerchantDevice merchantDevice = new MerchantDevice();
				merchantDevice.setDeviceId(deviceId);
				merchantDevice.setMerchantId(merchantId);
				this.merchantDeviceDao.insert(merchantDevice);
			}
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean gameSave(Map<String, Object> params) {
		String merchantId = StringUtil.toStr(params.get("merchantId"));
		String gameIds = StringUtil.toStr(params.get("gameIds"));
		this.omsMerchantDao.cleanMerchantGame(params);
		if (StringUtils.isNotBlank(gameIds)) {
			String[] gameIdList = gameIds.split(",");
			for (String gameId : gameIdList) {
				MerchantGame merchantGame = new MerchantGame();
				merchantGame.setGameId(gameId);
				merchantGame.setMerchantId(merchantId);
				this.merchantGameDao.insert(merchantGame);
			}
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean advertSave(Map<String, Object> params) {
		String merchantId = StringUtil.toStr(params.get("merchantId"));
		String advertIds = StringUtil.toStr(params.get("advertIds"));
		this.omsMerchantDao.cleanMerchantAdvert(params);
		if (StringUtils.isNotBlank(advertIds)) {
			String[] advertIdList = advertIds.split(",");
			for (String typeId : advertIdList) {
				MerchantAdvertType merchantAdvertType = new MerchantAdvertType();
				merchantAdvertType.setType(NumberUtil.toInt(typeId));
				merchantAdvertType.setMerchantId(merchantId);
				this.merchantAdvertTypeDao.insert(merchantAdvertType);
			}
		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean poolSave(Map<String, Object> params) {
		String merchantId = StringUtil.toStr(params.get("merchantId"));
		Integer poolSize = NumberUtil.toInt(params.get("poolSize"));
		MerchantPool pool = this.merchantPoolDao.selectByPrimaryKey(merchantId);
		if (pool == null) {
			pool = new MerchantPool();
			pool.setId(merchantId);
			pool.setUsedSize(0);
			pool.setPoolSize(poolSize);
			this.merchantPoolDao.insert(pool);
		} else {
			pool.setPoolSize(poolSize);
			this.merchantPoolDao.updateByPrimaryKey(pool);
		}
		return ResResultBean.success();
	}

}
