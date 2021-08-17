package com.bl.nop.cis.dto;

import java.io.Serializable;
import java.util.List;

public class MenuTreeDto implements Serializable {

	private static final long serialVersionUID = 8072477614709789237L;

	private String text;

	private String id;

	private String url;

	private String parMenuId;

	private String icon;

	private String odindex;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParMenuId() {
		return parMenuId;
	}

	public void setParMenuId(String parMenuId) {
		this.parMenuId = parMenuId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getOdindex() {
		return odindex;
	}

	public void setOdindex(String odindex) {
		this.odindex = odindex;
	}

	public List<MenuTreeDto> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTreeDto> children) {
		this.children = children;
	}

	private List<MenuTreeDto> children;

}
