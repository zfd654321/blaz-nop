package com.bl.nop.tts.service.impl;

import java.io.File;
import java.util.Date;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.tts.service.CleanJobService;
import com.bl.nop.tts.util.PropertyUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "CleanJobService")
public class CleanJobServiceImpl implements CleanJobService {

    private final static Logger log = LoggerFactory.getLogger(CleanJobServiceImpl.class);

    @Override
    public ResResultBean DayCleanJob(Date cleanDate) {

        log.info(">>>>>>>>>>>>>>>>>DayCleanJob 执行...");
        String DirName = DateUtil.formatDate(cleanDate, "yyyyMMdd");
        String Path = PropertyUtil.getProperty("filePath");
        File tcsInterimFile = new File(Path + "tcs/interim/" + DirName);
        if (tcsInterimFile.exists()) {
            tcsInterimFile.delete();
        }

        File omsInterimFile = new File(Path + "oms/interim/" + DirName);
        if (omsInterimFile.exists()) {
            omsInterimFile.delete();
        }
        log.info(">>>>>>>>>>>>>>>>>DayCleanJob 结束...");
        return ResResultBean.success();
    }

}
