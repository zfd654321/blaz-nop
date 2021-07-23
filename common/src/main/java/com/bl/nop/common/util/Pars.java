package com.bl.nop.common.util;

import java.util.HashMap;
import java.util.Map;

public class Pars {
	
	private Map<String, Object> data;
	
	private Pars(){
		
	} 
	
	public static Pars create(String key, Object value) {
		Pars pars = new Pars();
		pars.data = new HashMap<String, Object>();
		pars.data.put(key, value);
		return pars;
	}
	
	public static Pars create() {
		Pars pars = new Pars();
		pars.data = new HashMap<String, Object>();
		return pars;
	}
	
	public Pars put(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
	
	public Map<String, Object> toMap() {
		return this.data;
	}
}
