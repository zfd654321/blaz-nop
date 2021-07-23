package com.bl.nop.tcs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {
	
	private static final String path = "config/config.properties";
	private static Logger log = LoggerFactory.getLogger(PropertyUtil.class);
	private static Properties props = new Properties();
    static {
    	init();
    }
    
    private static void init() {
    	Reader in = null;
        try {
        	log.info("开始加载"+path);
        	InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(path);
        	in = new BufferedReader(new InputStreamReader(inputStream));
            props.load(in);
            log.info("加载"+path+"，完成");
        } catch (Exception e) {
            log.error("config.properties is blank, path: "+path, e);
        } finally {
        	if(null != in) {
        		try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
    /**
     * 获取指定key的value
     * @param key
     * @return
     */
    public static String getProperty(String key)  {
        return props.getProperty(key);
    }
    public static String getValue(String key)  {
    	return props.getProperty(key);
    }
    
    public static void main(String[] args) {
    	init();
	}
}
