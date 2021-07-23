package com.bl.nop.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class FileMD5Util {
	
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

	public static void main(String[] args) throws FileNotFoundException {
		File file=new File("G://01.mp4");
		String md5=getMd5ByFile(file);
		System.out.println(md5);
	}
}
