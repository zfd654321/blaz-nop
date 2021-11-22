package com.bl.nop.tts.quartz;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.tts.service.DeviceJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 每天要执行任务
 */
public class HourJob {

	@Autowired
	private DeviceJobService hourJobService;
	private static Logger log = LoggerFactory.getLogger(HourJob.class);

	/**
	 * 具体的任务
	 * 
	 * @throws ParseException
	 */
	public void doJob() throws ParseException {
		log.info(">>>>>>>>>>>>>HourJob 执行...");
		Date now=new Date();
        Date statsTime=DateUtil.addHour(now, -1);
        Integer statsHour=DateUtil.getHourOfDay(statsTime);
        String statsDate=DateUtil.dateToStrShort(statsTime);
		Map<String,Object> params=new HashMap<>();
		params.put("statsHour", statsHour);
		params.put("statsDate", statsDate);
		// 执行每小时设备统计任务
		ResResultBean result=this.hourJobService.deviceHourStatic(params);
		log.info(result.toString());


		log.info(">>>>>>>>>>>>>HourJob 结束...");
	}



}
