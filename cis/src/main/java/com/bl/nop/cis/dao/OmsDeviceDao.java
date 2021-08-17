package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.cis.dto.GameNetresDto;
import com.bl.nop.entity.advert.Advert;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DevicePc;
import com.bl.nop.entity.game.Game;

public interface OmsDeviceDao {
    List<Device> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);

    List<DevicePc> freePcList(Map<String, Object> params);

    List<Advert> deviceAdvertList(Map<String, Object> params);

    void cleanDeviceAdvert(String deviceId);

    List<Game> deviceGameList(Map<String, Object> params);

    List<GameNetresDto> deviceGameNetresList(Map<String, Object> params);

    void cleanDeviceGame(String deviceId);

    void cleanDeviceGameNetres(String deviceId);

}
