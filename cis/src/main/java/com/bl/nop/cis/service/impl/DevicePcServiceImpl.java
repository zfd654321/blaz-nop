package com.bl.nop.cis.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bl.nop.cis.api.DevicePcService;
import com.bl.nop.cis.dao.OmsDevicePcDao;
import com.bl.nop.cis.util.FileUtil;
import com.bl.nop.cis.util.PropertyUtil;
import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.common.util.Page;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.device.DevicePcDao;
import com.bl.nop.entity.device.DevicePc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("devicePcService")
public class DevicePcServiceImpl implements DevicePcService {

	private final static Logger log = LoggerFactory.getLogger(DevicePcServiceImpl.class);

	@Autowired
	private DevicePcDao devicePcDao;
	@Autowired
	private OmsDevicePcDao omsDevicePcDao;

	private static final String ERROR_CODE = "021";

	@Override
	public ResResultBean queryByPage(Map<String, Object> param) {
		int pageNo = NumberUtil.toInt(param.get("pageNo"), 0);
		int pageSize = NumberUtil.toInt(param.get("pageSize"), Page.DEFAULT_PAGE_SIZE);
		Page<DevicePc> page = new Page<DevicePc>(pageNo, pageSize);

		int start = page.getStart();
		if (start < 0) {
			return ResResultBean.success(page);
		}

		int count = this.omsDevicePcDao.findItemCount(param);
		page.setTotalCount(count);
		if (page.getTotalPage() < pageNo) {
			page.setCurrentPage(page.getTotalPage());
		}

		param.put("startNum", start);
		param.put("pageSize", pageSize);
		List<DevicePc> list = this.omsDevicePcDao.findItemByPage(param);
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
		String remarks = StringUtil.toStr(params.get("remarks"));
		String createdBy = StringUtil.toStr(params.get("createdBy"));
		Date now = new Date();
		DevicePc reitem = this.devicePcDao.selectByPrimaryKey(id);

		if (edit) {// 修改
			DevicePc item = reitem;
			item.setRemarks(remarks);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.devicePcDao.updateByPrimaryKey(item);

		} else {// 新增
			if (reitem != null) {
				return ResResultBean.error(ERROR_CODE + "002", "该机器码已存在");
			}
			log.info("新增PC>>>>>pc_id:" + id);
			// 生成pc激活文件
			String filePath = "forever/license/" + id + ".lisence";
			String requestUrl = MessageFormat.format(PropertyUtil.getProperty("license_request_url"), id);
			String license = FileUtil.saveImageToDisk(filePath, requestUrl);
			DevicePc item = new DevicePc();
			item.setId(id);
			item.setRemarks(remarks);
			item.setStatus(1);
			item.setLicense(license);
			item.setCreatedAt(now);
			item.setCreatedBy(createdBy);
			item.setUpdatedAt(now);
			item.setUpdatedBy(createdBy);
			this.devicePcDao.insert(item);

		}
		return ResResultBean.success();
	}

	@Override
	public ResResultBean delete(Map<String, Object> params) {
		String id = StringUtil.toStr(params.get("id"));
		this.devicePcDao.deleteByPrimaryKey(id);
		return ResResultBean.success();
	}

}
