package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.cis.dto.GameNetresDto;
import com.bl.nop.entity.advert.Advert;
import com.bl.nop.entity.device.Device;
import com.bl.nop.entity.device.DeviceAdvert;
import com.bl.nop.entity.device.DeviceGame;
import com.bl.nop.entity.device.DeviceGameNetres;
import com.bl.nop.entity.device.DeviceLog;
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

    List<DeviceAdvert> queryDeviceAdvert(Map<String, Object> params);

    List<DeviceGame> queryDeviceGame(Map<String, Object> params);

    List<DeviceGameNetres> queryDeviceGameNetres(Map<String, Object> params);

    List<DeviceLog> deviceLogList(Map<String, Object> params);

}
