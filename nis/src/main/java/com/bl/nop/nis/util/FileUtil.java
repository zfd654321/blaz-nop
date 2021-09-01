package com.bl.nop.nis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bl.nop.common.util.Md5Util;

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

	public static String saveImageToDisk(String filePath, String url_path) {
		String resultStr = "";
		InputStream inputStream = null;
		inputStream = getInputStream(url_path);// 调用getInputStream()函数。
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(PropertyUtil.getProperty("filePath") + filePath);// 初始化一个FileOutputStream对象。
			while ((len = inputStream.read(data)) != -1) {// 循环读取inputStream流中的数据，存入文件流fileOutputStream
				fileOutputStream.write(data, 0, len);
			}
			resultStr = PropertyUtil.getProperty("fileUrl") + filePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {// finally函数，不管有没有异常发生，都要调用这个函数下的代码
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();// 记得及时关闭文件流
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();// 关闭输入流
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultStr;

	}

	private static InputStream getInputStream(String url_path) {
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(url_path);// 创建的URL
			if (url != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection();// 打开链接
				httpURLConnection.setConnectTimeout(3000);// 设置网络链接超时时间，3秒，链接失败后重新链接
				httpURLConnection.setDoInput(true);// 打开输入流
				httpURLConnection.setRequestMethod("GET");// 表示本次Http请求是GET方式
				int responseCode = httpURLConnection.getResponseCode();// 获取返回码
				if (responseCode == 200) {// 成功为200
					// 从服务器获得一个输入流
					inputStream = httpURLConnection.getInputStream();
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;

	}

	public static JSONArray traverseFoloder(JSONArray array, String path, String repath) throws FileNotFoundException {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("空文件夹");
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("文件夹：" + file2.getAbsolutePath());
						traverseFoloder(array, file2.getAbsolutePath(),repath);
					} else {
						System.out.println("文件：" + file2.getAbsolutePath());
						JSONObject oj = new JSONObject();
						oj.put("name", file2.getName());
						oj.put("md5", Md5Util.getMd5ByFile(file2));
						oj.put("length", file2.length());
						oj.put("path", file2.getAbsolutePath().replace(repath, ""));
						array.add(oj);
					}
				}
			}
		}
		return array;
	}

	public static String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
			if (value.length() != 32) {
				int value_length = 32 - value.length();
				StringBuffer str = new StringBuffer();
				for (int i = 0; i < value_length; i++) {
					str.append("0");
				}
				str.append(value);
				value = str.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
}
