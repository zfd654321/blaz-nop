package com.bl.nop.cis.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bl.nop.cis.api.DataService;
import com.bl.nop.cis.dao.OmsDataDao;
import com.bl.nop.cis.dto.DeviceDataDto;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.entity.data.DataSumDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataService")
public class DataServiceImpl implements DataService {

	@Autowired
	private OmsDataDao omsDataDao;

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		return null;
	}

	@Override
	public ResResultBean showData(Map<String, Object> params) {
		JSONObject dataContent = new JSONObject();
		String statsDate = StringUtil.toStr(params.get("statsDate"));
		if (statsDate == null) {
			Date now = new Date();
			Date sDate = DateUtil.addHour(now, -25);
			statsDate = DateUtil.dateToStrShort(sDate);
		}

		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("statsDate", statsDate);
		List<DeviceDataDto> topList = this.omsDataDao.queryShowTopList(queryParams);
		List<DataSumDay> allData = this.omsDataDao.queryShowAllData(queryParams);
		dataContent.put("topList", topList);
		dataContent.put("allData", allData);

		return ResResultBean.success(dataContent);
	}

}
