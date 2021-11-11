package com.bl.nop.tts.quartz;

import java.text.ParseException;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.tts.service.HourJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 每天要执行任务
 */
public class MonthJob {

	@Autowired
	private HourJobService hourJobService;
	private static Logger log = LoggerFactory.getLogger(MonthJob.class);

	/**
	 * 具体的任务
	 * 
	 * @throws ParseException
	 */
	public void doJob() throws ParseException {
		log.info(">>>>>>>>>>>>>HourJob 执行...");

		// 执行每小时设备统计任务
		ResResultBean result=this.hourJobService.deviceHourStatic();
		log.info(result.toString());


		log.info(">>>>>>>>>>>>>HourJob 结束...");
	}



}
