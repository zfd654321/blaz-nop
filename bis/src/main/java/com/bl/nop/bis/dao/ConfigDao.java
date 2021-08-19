package com.bl.nop.bis.dao;

import java.util.List;

import com.bl.nop.bis.dto.AdvertDto;
import com.bl.nop.bis.dto.NetresDto;
import com.bl.nop.entity.device.DeviceRequest;
import com.bl.nop.entity.game.Game;

import org.springframework.stereotype.Repository;

@Repository
public interface ConfigDao {

    List<DeviceRequest> queryDeviceRequestList();

    List<AdvertDto> queryDeviceAdvertList(String deviceId);

    List<Game> queryDeviceGameList(String deviceId);

    List<NetresDto> queryDeviceGameNetresList(String deviceId);

}
