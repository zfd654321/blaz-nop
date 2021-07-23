package com.bl.nop.tcs.util;

public class WeixinAccessToken {

	private String access_token = "";
	
	private int expires_in = 0;
	
	private long createTime = 0l;

	public WeixinAccessToken() {
		
	}
	
	public WeixinAccessToken(String access_token, int expires_in) {
		this.access_token = access_token;
		this.expires_in = expires_in * 1000;
		this.createTime = System.currentTimeMillis();
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
		this.createTime = System.currentTimeMillis();
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in * 1000;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public boolean isOvertime() {
		if(null == access_token || "".equals(access_token)) {
			return true;
		}
		return createTime + expires_in <= System.currentTimeMillis();
	}
}
