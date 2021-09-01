package com.bl.nop.nis.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface PhotoService {
	/**
	 * 绑定用户照片/排行榜信息
	 * 
	 * @param params
	 * @return
	 */
	public JSONObject bondPhoto(Map<String, Object> params);

	/**
	 * 用户照片列表
	 * 
	 * @param params
	 * @return
	 */
	public JSONObject photoList(Map<String, Object> params);

	/**
	 * 用户排行榜列表
	 * 
	 * @param params
	 * @return
	 */
	public JSONObject rankList(Map<String, Object> params);

	/**
	 * 排行榜详情
	 * 
	 * @param params
	 * @return
	 */
	public JSONObject rankInfo(Map<String, Object> params);

    public JSONObject indexData(Map<String, Object> params);

}
