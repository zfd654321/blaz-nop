package com.bl.nop.tcs.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.Md5Util;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	public static Map<String, Object> saveUploadPhoto(MultipartFile file, Map<String, Object> params)
			throws IOException {
		Integer size = Integer.valueOf("" + file.getSize());// 文件大小
		String extName = ".jpg";// 文件后缀
		String fileId = "PHOTO" + DateUtil.getStringYMDHMS();
		String pathDir = PropertyUtil.getProperty("filePath") + "interim/" + DateUtil.getStringDateShortYMD()
				+ "/photo/";
		String urlDir = PropertyUtil.getProperty("fileUrl") + "interim/" + DateUtil.getStringDateShortYMD() + "/photo/";
		File dir = new File(pathDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String path = pathDir + fileId + extName;
		String url = urlDir + fileId + extName;
		FileCopyUtils.copy(file.getBytes(), new File(path));
		File fileL = new File(path);
		String md5 = Md5Util.getMd5ByFile(fileL);
		params.put("md5", md5);
		params.put("url", url);
		params.put("size", size);
		params.put("type", 1);
		return params;
	}

	public static Map<String, Object> saveUploadVideo(MultipartFile file, Map<String, Object> params)
			throws IOException {
		Integer size = Integer.valueOf("" + file.getSize());// 文件大小
		String extName = ".mp4";// 文件后缀
		String fileId = "PHOTO" + DateUtil.getStringYMDHMS();
		String pathDir = PropertyUtil.getProperty("filePath") + "interim/" + DateUtil.getStringDateShortYMD()
				+ "/photo/";
		String urlDir = PropertyUtil.getProperty("fileUrl") + "interim/" + DateUtil.getStringDateShortYMD() + "/photo/";
		File dir = new File(pathDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String path = pathDir + fileId + extName;
		String url = urlDir + fileId + extName;
		FileCopyUtils.copy(file.getBytes(), new File(path));
		File fileL = new File(path);
		String md5 = Md5Util.getMd5ByFile(fileL);
		params.put("md5", md5);
		params.put("url", url);
		params.put("size", size);
		params.put("type", 2);
		return params;
	}
}
