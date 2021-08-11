package com.bl.nop.cis.dao;

import java.util.List;
import java.util.Map;

import com.bl.nop.entity.resources.Resources;

public interface OmsResourcesDao {
    List<Resources> findItemByPage(Map<String, Object> param);

    int findItemCount(Map<String, Object> param);
}
