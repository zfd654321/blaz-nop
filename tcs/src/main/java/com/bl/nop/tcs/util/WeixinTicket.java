package com.bl.nop.tcs.util;

public class WeixinTicket {

	private String ticket = "";
	private int expires_in = 0;
	private long createTime = 0l;
	
	public String getTicket() {
		return ticket;
	}
	
	public void setTicket(String ticket) {
		this.ticket = ticket;
		this.createTime = System.currentTimeMillis();
	}
	
	public int getExpires_in() {
		return expires_in;
	}
	
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in * 1000;
	}
	
	public boolean isOvertime() {
		if(null == ticket || "".equals(ticket)) {
			return true;
		}
		return createTime + expires_in <= System.currentTimeMillis();
	}
}
