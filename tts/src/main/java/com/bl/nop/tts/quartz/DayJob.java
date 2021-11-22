package com.bl.nop.tts.quartz;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.tts.service.CleanJobService;
import com.bl.nop.tts.service.DeviceJobService;
import com.bl.nop.tts.service.GameJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 每天要执行任务
 */
public class DayJob {

	@Autowired
	private DeviceJobService deviceJobService;
	@Autowired
	private GameJobService gameJobService;
	@Autowired
	private CleanJobService cleanJobService;
	private static Logger log = LoggerFactory.getLogger(DayJob.class);

	/**
	 * 具体的任务
	 * 
	 * @throws ParseException
	 */
	public void doJob() throws ParseException {
		log.info(">>>>>>>>>>>>>DayJob 执行...");

		
		Date now=new Date();
        Date statsTime=DateUtil.addDay(now, -1);
        String statsDate=DateUtil.dateToStrShort(statsTime);
		Map<String,Object> params=new HashMap<>();
		params.put("statsDate", statsDate);

		// 执行每天设备统计任务
		ResResultBean result=this.deviceJobService.deviceDayStatic(params);
		log.info(result.toString());

		// 执行每天游戏统计任务
		result=this.gameJobService.gameDayStatic(params);
		log.info(result.toString());

		//执行清理临时文件任务
		Date cleanDate=DateUtil.addDay(now, -31);
		result=this.cleanJobService.DayCleanJob(cleanDate);
		log.info(result.toString());


		log.info(">>>>>>>>>>>>>DayJob 结束...");
	}



}
