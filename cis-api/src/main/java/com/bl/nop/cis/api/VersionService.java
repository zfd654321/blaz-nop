package com.bl.nop.cis.api;

import java.util.Map;

import com.bl.nop.common.bean.ResResultBean;

public interface VersionService {
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
     * @return
     */
    public ResResultBean save(Map<String, Object> params);

    /**
     * 增量更新-处理版本文件
     * 
     * @param params
     * @return
     */
    public ResResultBean build(Map<String, Object> params);

    /**
     * 增量更新-校验版本文件
     * 
     * @param params
     * @return
     */
    public ResResultBean check(Map<String, Object> params);

    /**
     * 增量更新-添加版本数据
     * 
     * @param params
     * @return
     */
    public ResResultBean add(Map<String, Object> params);

    /**
     * 增量更新-清理文件
     * 
     * @param params
     * @return
     */
    public ResResultBean clean(Map<String, Object> params);

    /**
     * 删除信息
     * 
     * @param params
     * @return
     */
    public ResResultBean delete(Map<String, Object> params);

    /**
     * 分页查询下载器信息
     * 
     * @param param
     * @return
     */
    public ResResultBean downLoaderList(Map<String, Object> param);

    /**
     * 保存下载器信息
     * 
     * @param params
     * @return
     */
    public ResResultBean downloaderSave(Map<String, Object> params);

    /**
     * 回滚下载器信息
     * 
     * @param params
     * @return
     */
    public ResResultBean downloaderDelete(Map<String, Object> params);
}
