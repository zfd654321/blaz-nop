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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.bl.nop.common.util.DateUtil;
import com.bl.nop.common.util.Md5Util;
import com.bl.nop.common.util.StringUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

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

	public static void getImageResourcesCutImage(String imgSrc, String imgdist) {
		int widthdist = 0;
		int heightdist = 0;
		try {
			File srcfile = new File(imgSrc);
			// ??????????????????????????????
			if (!srcfile.exists()) {// ???????????????????????????
				return;
			} else {
				// ???????????????????????????????????????
				int[] results = getImgWidthHeight(srcfile);
				if (results == null || results[0] == 0 || results[1] == 0) {
					return;
				} else {
					// ???????????????????????????????????????????????????????????????
					widthdist = (int) (results[0]);
					heightdist = (int) (results[1]);
					if (widthdist > heightdist) {// ???>???
						BigDecimal a = new BigDecimal(100);
						BigDecimal b = new BigDecimal(widthdist);
						BigDecimal rate = a.divide(b, 2, RoundingMode.HALF_UP);
						widthdist = 100;
						BigDecimal c = new BigDecimal(heightdist);
						heightdist = c.multiply(rate).intValue();
					} else {// ???>???
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

			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
	}

	public static void getVideoResourcesCutImage(String imgSrc, String imgdist) {
		try {
			StringBuffer ffmpegStr = new StringBuffer();
			ffmpegStr.append("time ffmpeg -ss 00:00:05 -i ");
			ffmpegStr.append(imgSrc);
			ffmpegStr.append(" -f image2 -frames:v 1 -y -s 100*100 ");
			ffmpegStr.append(imgdist);
			System.out.println(ffmpegStr.toString());
			Runtime.getRuntime().exec(ffmpegStr.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] getImgWidthHeight(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			// ?????????????????????
			is = new FileInputStream(file);
			// ???????????????????????????????????????
			src = ImageIO.read(is);
			result[0] = src.getWidth(null); // ??????????????????
			result[1] = src.getHeight(null);// ??????????????????
			is.close(); // ???????????????
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

	public static void unZipIt(String file, String outputFolder) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		try {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File entryDestination = new File(outputFolder, entry.getName());
				if (entry.isDirectory()) {
					entryDestination.mkdirs();
				} else {
					entryDestination.getParentFile().mkdirs();
					InputStream in = zipFile.getInputStream(entry);
					OutputStream out = new FileOutputStream(entryDestination);
					IOUtils.copy(in, out);
					IOUtils.closeQuietly(in);
					out.close();
				}
			}
		} finally {
			zipFile.close();
		}
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 */
	public static void deletefile(String delpath) {
		try {
			File file = new File(delpath);
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + File.separator + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletefile(delpath + File.separator + filelist[i]);
					}
				}
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> saveResourceFile(MultipartFile file, Map<String, Object> params) throws IOException {
		String type = StringUtil.toStr(params.get("type"));
		Integer size = Integer.valueOf("" + file.getSize());// ????????????
		String filename = file.getOriginalFilename();
		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();// ????????????
		String fileId = "CMSFile" + DateUtil.getStringYMDHMS();
		String pathDir = PropertyUtil.getProperty("filePath") + "forever/resouces/";
		String urlDir = PropertyUtil.getProperty("fileUrl") + "forever/resouces/";
		String path = pathDir + type + "/" + fileId + extName;
		String url = urlDir + type + "/" + fileId + extName;
		FileCopyUtils.copy(file.getBytes(), new File(path));
		File fileL = new File(path);
		String md5 = Md5Util.getMd5ByFile(fileL);
		String thumbnailpath = pathDir + type + "/" + fileId + "_cut.png";
		String thumbnailurl = urlDir + type + "/" + fileId + "_cut.png";
		params.put("md5", md5);
		params.put("path", path);
		params.put("url", url);
		params.put("size", size);
		params.put("ext", extName);
		switch (type) {
			case "img": {
				FileUtil.getImageResourcesCutImage(path, thumbnailpath);
				break;
			}
			case "video": {
				FileUtil.getVideoResourcesCutImage(path, thumbnailpath);
				break;
			}
			case "other": {
				thumbnailurl = urlDir + "/file_default.jpg";
				break;
			}
		}
		params.put("thumbnail", thumbnailurl);
		return params;
	}

}
