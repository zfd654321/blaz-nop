package com.bl.nop.cis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSONObject;

public class FileUtil {
	public static JSONObject getFileJson(String file) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 读取原始json文件
			// BufferedReader br = new BufferedReader(new FileReader(file));
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader read = new BufferedReader(isr);
			String tempString = null;
			String laststr = "";
			while ((tempString = read.readLine()) != null) {
				laststr += tempString;

			}
			isr.close();
			read.close();
			jsonObject = JSONObject.parseObject(laststr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		}
	}
}
