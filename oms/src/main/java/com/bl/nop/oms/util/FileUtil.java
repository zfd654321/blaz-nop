package com.bl.nop.oms.util;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

public class FileUtil {
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

	public static String getFileResourcesCutImage(String imgSrc, String imgdist) {
		String cutImageUrl = "";
		int widthdist = 0;
		int heightdist = 0;
		try {
			File srcfile = new File(imgSrc);
			// 检查图片文件是否存在
			if (!srcfile.exists()) {// 确认源图片是否存在
				return cutImageUrl;
			} else {
				// 获得源图片的宽高存入数组中
				int[] results = getImgWidthHeight(srcfile);
				if (results == null || results[0] == 0 || results[1] == 0) {
					return cutImageUrl;
				} else {
					// 按比例缩放或扩大图片大小，将浮点型转为整型
					widthdist = (int) (results[0]);
					heightdist = (int) (results[1]);
					if (widthdist > heightdist) {// 宽>长
						BigDecimal a = new BigDecimal(100);
						BigDecimal b = new BigDecimal(widthdist);
						BigDecimal rate = a.divide(b, 2, RoundingMode.HALF_UP);
						widthdist = 100;
						BigDecimal c = new BigDecimal(heightdist);
						heightdist = c.multiply(rate).intValue();
					} else {// 长>宽
						BigDecimal a = new BigDecimal(100);
						BigDecimal b = new BigDecimal(heightdist);
						BigDecimal rate = a.divide(b, 2, RoundingMode.HALF_UP);
						heightdist = 100;
						BigDecimal c = new BigDecimal(widthdist);
						widthdist = c.multiply(rate).intValue();
					}
				}
				BufferedImage bufferedImage = ImageIO.read(srcfile);
				Image image = bufferedImage.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH);
				BufferedImage outputImage = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);
				Graphics graphics = outputImage.getGraphics();
				graphics.drawImage(image, 0, 0, null);
				graphics.dispose();
				ImageIO.write(outputImage, "jpg", new File(imgdist));
				cutImageUrl = imgdist.replace(PropertyUtil.getProperty("saveDir"), PropertyUtil.getProperty("fileDir"));

			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return cutImageUrl;
	}

	public static int[] getImgWidthHeight(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			// 获得文件输入流
			is = new FileInputStream(file);
			// 从流里将图片写入缓冲图片区
			src = ImageIO.read(is);
			result[0] = src.getWidth(null); // 得到源图片宽
			result[1] = src.getHeight(null);// 得到源图片高
			is.close(); // 关闭输入流
		} catch (Exception ef) {
			try {
				ThumbnailConvert tc = new ThumbnailConvert();
				tc.setCMYK_COMMAND(file.getPath());
				Image image = null;
				image = Toolkit.getDefaultToolkit().getImage(file.getPath());
				MediaTracker mediaTracker = new MediaTracker(new Container());
				mediaTracker.addImage(image, 0);
				mediaTracker.waitForID(0);
				result[0] = image.getWidth(null);
				result[1] = image.getHeight(null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	public static String getRequestPayload(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = req.getReader();) {
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
