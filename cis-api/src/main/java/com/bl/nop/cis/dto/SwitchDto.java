package com.bl.nop.cis.dto;

import com.bl.nop.entity.device.DeviceSwitch;

public class SwitchDto extends DeviceSwitch {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pcId;

	private String name;

	public String getPcId() {
		return pcId;
	}

	public void setPcId(String pcId) {
		this.pcId = pcId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
