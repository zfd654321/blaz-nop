package com.bl.nop.common.bean;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class CommandBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String command;
	
	private Object data = new HashMap<String, Object>();
	
	private Map<String, Object> head = new HashMap<String, Object>();
	
	public CommandBean() {
		this.head.put("data", data);
	}

	public CommandBean(String command, Object data) {
		this.command = command;
		this.data = data;
		this.head.put("command", command);
		this.head.put("data", data);
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
		this.head.put("command", command);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
		this.head.put("data", data);
	}
	
	public void putHead(String key, Object value) {
		this.head.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void putData(String key, Object value) {
		((HashMap<String, Object>) this.data).put(key, value);
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this.head);
	}
}
