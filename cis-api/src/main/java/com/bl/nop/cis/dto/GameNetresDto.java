package com.bl.nop.cis.dto;

import com.bl.nop.entity.game.GameNetres;

public class GameNetresDto extends GameNetres {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String resUrl;

	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

}
