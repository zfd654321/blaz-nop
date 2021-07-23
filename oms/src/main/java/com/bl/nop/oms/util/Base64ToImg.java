package com.bl.nop.oms.util;

// import java.io.FileOutputStream;
// import java.io.OutputStream;
// import java.text.SimpleDateFormat;
// import java.util.Date;
// import java.util.Random;

// import sun.misc.BASE64Decoder;

/**
 * Created by Mr Pan on 2019/5/20.
 */
// public class Base64ToImg {
// 	public static String GenerateImage(String imgStr, String imgFilePath, String rootPath) throws Exception {
// 		if (imgStr == null) // 图像数据为空
// 			return "";
// 		BASE64Decoder decoder = new BASE64Decoder();

// 		// Base64解码,对字节数组字符串进行Base64解码并生成图片
// 		imgStr = imgStr.replaceAll(" ", "+");
// 		System.out.println(imgStr);
// 		byte[] b = decoder.decodeBuffer(imgStr.replace("data:image/jpeg;base64,", ""));
// 		for (int i = 0; i < b.length; ++i) {
// 			if (b[i] < 0) {// 调整异常数据
// 				b[i] += 256;
// 			}
// 		}
// 		String imgName = getRandomFileName() + ".jpg";
// 		String dbUrl = "";
// 		// 生成jpeg图片D:\test\attendance\src\main\webapp\assets\images\leave
// 		imgFilePath = imgFilePath + imgName;// 新生成的图片
// 		OutputStream out = new FileOutputStream(imgFilePath);
// 		out.write(b);
// 		out.flush();
// 		out.close();
// 		dbUrl = rootPath + imgName;
// 		return dbUrl;
// 	}

// 	public static String getRandomFileName() {

// 		SimpleDateFormat simpleDateFormat;

// 		simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

// 		Date date = new Date();

// 		String str = simpleDateFormat.format(date);

// 		Random random = new Random();

// 		int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

// 		return rannum + str;// 当前时间
// 	}
	
// 	public static void main(String[] args) {
// 		//这一步很重要很重要很重要，因为base64的数据会有data：base64img，
// 		String str = "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";
// 		 try {
// 			GenerateImage(str,"F:\\tupian\\tupian.png","");
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 		}
		
// 	}

// }
