package com.bl.nop.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeUtil {

	private static Logger log = Logger.getLogger(QRCodeUtil.class);
	private static final String CHARSET = "UTF-8";
	private static final String FORMAT_NAME = "png";
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	// LOGO宽度
	private static final int WIDTH = 400;
	// LOGO高度
	private static final int HEIGHT = 400;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String build(String qrData, String path) {
		String name = (UUID.randomUUID().toString())+"."+FORMAT_NAME;
		try {
			log.info("开始生成二维码，生成内容："+qrData+"，存放路径："+path);
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		    Map hints = new HashMap();
		    hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		    BitMatrix bitMatrix = multiFormatWriter.encode(qrData, BarcodeFormat.QR_CODE, WIDTH, HEIGHT,hints);
		    File file = new File(path, name);
		    int width = bitMatrix.getWidth();
		    int height = bitMatrix.getHeight();
		    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		    for (int x = 0; x < width; x++) {
		    	for (int y = 0; y < height; y++) {
		    		image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
		    	}
		    } 
		    ImageIO.write(image, FORMAT_NAME, file);
	        log.info("二维码生成完毕");
		} catch (Exception e) {
			log.error("生成二维码失败", e);
		}
		return name;
	}
	public static void main(String[] args) {
		String name = build("https://bltest.shishiar.cn/BLWXAV3.0/photo?html_tag=XMXXXXXXXX120190415171042&device_id=XMXXXXXXXX1&order_id=XMXXXXXXXX120190415145004", "d:/");
		System.out.println(name);
	}
}
