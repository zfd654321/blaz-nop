package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface AdvertService {
    /**
     * 分页查询信息
     * 
     * @param param
     * @return
     */
    public ResResultBean queryByPage(Map<String, Object> param);

    /**
     * 保存信息
     * 
     * @param params
     * 
     * @param params
     * @return
     */
    public ResResultBean save(Map<String, Object> params);

    /**
     * 删除信息
     * 
     * @param params
     * 
     * @param params
     * @return
     */
    public ResResultBean delete(Map<String, Object> params);

    /**
     * 分页查询模板信息
     * 
     * @param param
     * @return
     */
    public ResResultBean queryTypeByPage(Map<String, Object> param);

    /**
     * 保存模板信息
     * 
     * @param params
     * 
     * @param params
     * @return
     */
    public ResResultBean saveType(Map<String, Object> params);
}
