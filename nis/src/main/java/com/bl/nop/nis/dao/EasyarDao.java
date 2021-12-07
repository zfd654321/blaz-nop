package com.bl.nop.nis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.wx.EasyarLog;

import org.springframework.stereotype.Repository;

@Repository
public interface EasyarDao {

    public List<EasyarLog> findItemByPage(Map<String, Object> params);

    int findItemCount(Map<String, Object> param);

}
