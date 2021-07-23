package com.bl.nop.tcs.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bl.nop.common.util.DateUtil;

public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 创建磁盘文件夹
	 * 
	 * @param business_type
	 * @param is_forever
	 * @param user_id
	 * @param file_type
	 * @param terrace_type
	 * @param file_suffix
	 * @param fileID
	 * @param fileName
	 * @param fileFolderStr
	 * @return
	 */
	public static Map<String, String> createfileFolder(String business_type, String is_forever, String user_id, String file_type,
			String terrace_type, String file_suffix, String remote_file_service) {
		StringBuffer fileFolder = new StringBuffer();// 文件夹
		String fileID = "";
		String fileName = "";
		
		fileFolder.append(remote_file_service).append("/").append(business_type).append("/")
				.append(is_forever).append("/");
		if (StringUtils.equals("business", business_type)) {// 商家文件
			fileID = user_id + DateUtil.formatDate(new Date(), "yyyyMMddHHmmssS");
			fileFolder.append(user_id).append("/");
			if (StringUtils.equals("forever", is_forever)) {// 永久保存
				fileFolder.append(file_type).append("/");
			} else if (StringUtils.equals("interim", is_forever)) {// 临时保存
				fileFolder.append(DateUtil.formatDate(new Date(), "yyyyMM")).append("/");
				fileFolder.append(file_type).append("/");
			}
		} else if (StringUtils.equals("self", business_type)) {// 自营文件
			fileID = terrace_type + user_id + DateUtil.formatDate(new Date(), "yyyyMMddHHmmssS");
			fileFolder.append(terrace_type).append("/");
			if (StringUtils.equals("forever", is_forever)) {// 永久保存
				fileFolder.append(file_type).append("/");
			} else if (StringUtils.equals("interim", is_forever)) {// 临时保存
				fileFolder.append(DateUtil.formatDate(new Date(), "yyyyMM")).append("/");
				fileFolder.append(file_type).append("/");
			}
		}

		fileName = fileID + file_suffix;
		// 先确认文件夹是否存在，不存在则创建
		String fileFolderStr = "";
		try {
			fileFolderStr = fileFolder.toString();
			log.info("创建文件夹路径为："+fileFolderStr);
			File f1 = new File(fileFolderStr);
			if (!f1.exists()) {// 文件夹是否存在
				f1.mkdirs();
			}
		} catch (Exception e) {
			log.error("创建文件夹路径为："+fileFolderStr+"，失败", e);
		}
		
		Map<String, String> fileMap = new HashMap<>();
		fileMap.put("fileID", fileID);
		fileMap.put("fileName", fileName);
		fileMap.put("fileFolderStr", fileFolderStr);
		return fileMap;
	}
}
