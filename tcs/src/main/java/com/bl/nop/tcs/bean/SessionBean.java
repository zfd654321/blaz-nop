package com.bl.nop.tcs.bean;

public class SessionBean {

	private String deviceId;
	
	private String sId;
	
	private String openid;
	
	private String gameId;
	
	private String orderId;

	public SessionBean() {
		
	}
	
	public SessionBean(String deviceId, String openid, String gameId, String sId, String orderId) {
		this.deviceId = deviceId;
		this.openid = openid;
		this.gameId = gameId;
		this.sId = sId;
		this.orderId = orderId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "{deviceId:"+deviceId+", openid:"+openid+", gameId:"+gameId+", sId:"+sId+", orderId:"+orderId+"}";
	}
	
}
