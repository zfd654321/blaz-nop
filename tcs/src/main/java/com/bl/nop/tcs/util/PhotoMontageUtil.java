package com.bl.nop.tcs.util;

import java.awt.image.BufferedImage;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bl.nop.common.util.DateUtil;

public class PhotoMontageUtil {
	private static Logger log = LoggerFactory.getLogger(PhotoMontageUtil.class);
	
	public static String getWXPhotoMontage(String photoUrls, String openid) {
		String wimgUrl = "";
		String saveFile = PropertyUtil.getValue("saveDir");
		String fileDir = PropertyUtil.getValue("fileDir");
		if (photoUrls.contains(",")) {
			String[] imagesPhoto = photoUrls.split(",");
			int len = imagesPhoto.length;
			if (len >= 3) {
				String bi1 = imagesPhoto[0];
				// + bi1.substring(53, bi1.length());
				String bi1Str = saveFile + bi1.replace(fileDir, "");

				String bi2 = imagesPhoto[1];
				String bi2Str = saveFile + bi2.replace(fileDir, "");

				String bi3 = imagesPhoto[2];
				String bi3Str = saveFile + bi3.replace(fileDir, "");
				wimgUrl = getNewPhotoMontage(bi1Str, bi2Str, bi3Str, openid);

			} else if (len == 2) {
				String bi1 = imagesPhoto[0];
				String bi1Str = saveFile + bi1.replace(fileDir, "");

				String bi2 = imagesPhoto[1];
				String bi2Str = saveFile + bi2.replace(fileDir, "");
				wimgUrl = getNewPhotoMontage(bi1Str, bi2Str, bi1Str, openid);
			}
		} else {// 说明只有一张图片
			String bi1Str = saveFile + photoUrls.replace(fileDir, "");
			wimgUrl = getNewPhotoMontage(bi1Str, bi1Str, bi1Str, openid);
		}
		return wimgUrl;
	}
	
	private static String getNewPhotoMontage(String bi1Str, String bi2Str, String bi3Str, String openId) {
		String imageUrl = null;
		try {
			log.info("合并图片，合并连接，"+bi1Str+","+bi2Str+","+bi3Str);
			String ym = DateUtil.formatDate(new Date(), "yyyyMM");
			String saveFile = PropertyUtil.getValue("saveDir");
			String fileDir = PropertyUtil.getValue("fileDir");
			BufferedImage bi1 = ImageMergeUtil.getBufferedImage(bi1Str);
			BufferedImage bi2 = ImageMergeUtil.getBufferedImage(bi2Str);
			BufferedImage bi3 = ImageMergeUtil.getBufferedImage(bi3Str);
			BufferedImage destImg = ImageMergeUtil.mergeImage(true, bi1, bi2, bi3);
			String imageName = DateUtil.getStringYMDHMS() + openId + ".jpg";
			ImageMergeUtil.saveImage(destImg, saveFile + PropertyUtil.getValue("photomontage_wx")+ym+"/"+PropertyUtil.getValue("photomontage"),
					imageName, "jpg");
			imageUrl = fileDir + PropertyUtil.getValue("photomontage_wx")+ym+"/"+PropertyUtil.getValue("photomontage") + imageName;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("合并图片失败", e);
		}
		log.info("水平合并完毕!");
		return imageUrl;
	}
}
