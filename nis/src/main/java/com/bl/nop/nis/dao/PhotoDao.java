package com.bl.nop.nis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.wx.WxRank;
import com.bl.nop.nis.dto.PhotoDto;
import com.bl.nop.nis.dto.RankDto;
import com.bl.nop.nis.dto.RankInfoDto;

import org.springframework.stereotype.Repository;

@Repository
public interface PhotoDao {

    public List<PhotoDto> getPhotoList(Map<String, Object> params);

    public List<RankDto> getRankList(Map<String, Object> params);

    public RankDto getRankGame(Map<String, Object> params);

    public List<RankInfoDto> getRankInfoList(Map<String, Object> params);

    public WxRank getWxRankById(Map<String, Object> queryParam);

}
