package com.bl.nop.bis.dto;

import com.bl.nop.entity.device.Device;

public class DeviceDto extends Device {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pcStatus;
	private String deviceSId;
	private String examineDesc;
	private String batchName;

	public String getPcStatus() {
		return pcStatus;
	}

	public void setPcStatus(String pcStatus) {
		this.pcStatus = pcStatus;
	}

	public String getDeviceSId() {
		return deviceSId;
	}

	public void setDeviceSId(String deviceSId) {
		this.deviceSId = deviceSId;
	}

	public String getExamineDesc() {
		return examineDesc;
	}

	public void setExamineDesc(String examineDesc) {
		this.examineDesc = examineDesc;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	
	

}
