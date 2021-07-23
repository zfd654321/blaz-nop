package com.bl.nop.common.exception;


public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;

	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String message, Throwable ex) {
		super(message, ex);
	}
	
	public BusinessException(String code, String message, Throwable ex) {
		super(message, ex);
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
