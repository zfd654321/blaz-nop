package com.bl.nop.tts.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bl.nop.common.bean.ResResultBean;
import com.bl.nop.dao.data.DataGameDayDao;
import com.bl.nop.entity.data.DataGame;
import com.bl.nop.entity.data.DataGameDay;
import com.bl.nop.tts.dao.GameJobDao;
import com.bl.nop.tts.service.GameJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "HourJobService")
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
        List<DataGame> list = this.gameJobDao.queryGameDayData(params);
        Map<String, DataGameDay> map = new HashMap<>();
        for (DataGame dataGameDay : list) {
            String deviceId = dataGameDay.getDeviceId();
            String gameId = dataGameDay.getGameId();
            Integer duration = dataGameDay.getDuration();

            DataGameDay gameDay = map.get(deviceId + "-" + gameId);
            if (gameDay == null) {
                gameDay = new DataGameDay();
                gameDay.setDeviceId(deviceId);
                gameDay.setGameId(gameId);
                gameDay.setFinishTime(0);
                gameDay.setLostTime(0);
                gameDay.setBreakTime(0);
            }
            switch (dataGameDay.getType()) {
            case 1:
                gameDay.setFinishTime(duration);
                break;

            case 2:
                gameDay.setLostTime(duration);
                break;

            case 3:
                gameDay.setBreakTime(duration);
                break;

            default:
                break;
            }
            map.put(deviceId + "-" + gameId, gameDay);
        }
        for (Entry<String, DataGameDay> gameDayEntry : map.entrySet()) {
            DataGameDay dataGameDay = gameDayEntry.getValue();
            this.dataGameDayDao.insert(dataGameDay);
        }

        log.info(">>>>>>>>>>>>>>>>>gameDayStatic 结束...");
        return null;
    }

}
