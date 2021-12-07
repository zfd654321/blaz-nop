package com.bl.nop.tts.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.StringUtil;
import com.bl.nop.dao.data.DataGameDayDao;
import com.bl.nop.entity.data.DataGameDay;
import com.bl.nop.tts.dao.GameJobDao;
import com.bl.nop.tts.dto.DataGameDto;
import com.bl.nop.tts.service.GameJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "GameJobService")
public class GameJobServiceImpl implements GameJobService {

    @Autowired
    private GameJobDao gameJobDao;

    @Autowired
    private DataGameDayDao dataGameDayDao;

    private final static Logger log = LoggerFactory.getLogger(GameJobServiceImpl.class);

    @Override
    public ResResultBean gameDayStatic(Map<String, Object> params) {
        log.info(">>>>>>>>>>>>>>>>>gameDayStatic 执行...");
        this.gameJobDao.cleanGameDayData(params);
        List<DataGameDto> list = this.gameJobDao.queryGameDayData(params);
        String statsDate=StringUtil.toStr(params.get("statsDate"));
        Map<String, DataGameDay> map = new HashMap<>();
        for (DataGameDto dataGameDay : list) {
            String deviceId = dataGameDay.getDeviceId();
            String gameId = dataGameDay.getGameId();
            Integer duration = dataGameDay.getDuration();
            Integer times=dataGameDay.getTimes();
            DataGameDay gameDay = map.get(deviceId + "-" + gameId);
            if (gameDay == null) {
                gameDay = new DataGameDay();
                gameDay.setDeviceId(deviceId);
                gameDay.setGameId(gameId);
                gameDay.setStatsDate(DateUtil.strToDateShort(statsDate));
                gameDay.setDuration(0);
                gameDay.setFinishTime(0);
                gameDay.setLostTime(0);
                gameDay.setBreakTime(0);
            }
            gameDay.setDuration(gameDay.getDuration()+duration);
            switch (dataGameDay.getType()) {
            case 1:
                gameDay.setFinishTime(times);
                break;

            case 2:
                gameDay.setLostTime(times);
                break;

            case 3:
                gameDay.setBreakTime(times);
                break;

            default:
                break;
            }
            map.put(deviceId + "-" + gameId, gameDay);
        }
        log.info(">>>>>>>>>>>>>>gameListSize:"+map.size());
        for (Entry<String, DataGameDay> gameDayEntry : map.entrySet()) {
            DataGameDay dataGameDay = gameDayEntry.getValue();
            this.dataGameDayDao.insert(dataGameDay);
        }

        log.info(">>>>>>>>>>>>>>>>>gameDayStatic 结束...");
        return ResResultBean.success();
    }

}
