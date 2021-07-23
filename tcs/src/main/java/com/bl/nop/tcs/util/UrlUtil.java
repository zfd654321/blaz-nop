package com.bl.nop.tcs.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.CharEncoding;

public class UrlUtil {
	public static String encode(String s) {
		return encode(s, CharEncoding.UTF_8);
	}

	public static String encode(String s, String encoding) {
		try {
			return URLEncoder.encode(s, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public static String decode(String s) {
		return decode(s, CharEncoding.UTF_8);
	}

	public static String decode(String s, String encoding) {
		try {
			return URLDecoder.decode(s, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
