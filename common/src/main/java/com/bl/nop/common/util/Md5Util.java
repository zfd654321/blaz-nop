package com.bl.nop.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

	/**
	 * 字符串加密成md5
	 * @param s
	 * @return
	 */
    public static String toMd5(String s) {
        try {
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();                
                try {
					algorithm.update(s.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
                return toHexString(algorithm.digest());
        } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
        }
    }
    /**
     * 加密字符串和加密盐加密成md5
     * @param s
     * @param encryptedSalt
     * @return
     */
    public static String toMd5(String s, String encryptedSalt) {
    	try {
    		MessageDigest algorithm = MessageDigest.getInstance("MD5");
    		algorithm.reset();                
    		try {
    			algorithm.update(s.concat(encryptedSalt).getBytes("utf-8"));
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}
    		return toHexString(algorithm.digest());
    	} catch (NoSuchAlgorithmException e) {
    		throw new RuntimeException(e);
    	}
    }
    
    
	private static String toHexString(byte[] bytes) {
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : bytes) {
	                hexString.append(Integer.toHexString((b & 0x000000FF) | 0xFFFFFF00).substring(6));//.append(separator);
	        }
	        return hexString.toString();
	}
	
	/**
	 * 文件内容加密md5
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
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
	
	/**
	 * 字节流加密md5
	 * @param in
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getMd5ByInputStream(InputStream in) throws FileNotFoundException {
		String value = null;
		
		try {
			
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] buffer=new byte[1024];
			int numRead=0;
			while ((numRead=in.read(buffer))>0){
				md5.update(buffer, 0, numRead);
			}
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
	
	protected static char hexDigits[] = { '0', '1','2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f'};
	public static void main(String[] args) throws Exception {
		String deviceId="XAR75520001";
		System.out.println(Md5Util.toMd5(deviceId + "BLAZ"));
	}
}
