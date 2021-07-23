package com.bl.nop.common.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ResResultBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SUCCESS = "00";
	
	private static final String ERROR = "01";
	
	private Object data;
    private String code = "";
    private String message = "";
    private boolean isSuccess = true;

	private ResResultBean(boolean isSuccess, String code, String message) {
    	this.code = code;
    	this.message = message;
    	this.data = (Object) new JSONObject();
    	this.isSuccess = isSuccess;
    }
	
    private ResResultBean(boolean isSuccess, String code, String message, Object data) {
    	this.code = code;
    	this.message = message;
    	this.data = data;
    	this.isSuccess = isSuccess;
    }
    
    /**  
     * �ɹ�ʱ��ĵ���? �����뷵�����?
     * @return  
     */  
    public static ResResultBean success(Object data){  
        return new ResResultBean(true, SUCCESS, "操作成功", data);  
    }  
    /**  
     * �ɹ�������Ҫ�������?  
     * @return  
     */  
    public static ResResultBean success(){  
        return new ResResultBean(true, SUCCESS, "操作成功");  
    }  
    
    public static ResResultBean error(){  
    	return new ResResultBean(false, ERROR, "操作失败");  
    }  
    
    public static ResResultBean error(Object data){  
    	return new ResResultBean(false, ERROR, "操作失败", data);  
    }  
    
    public static ResResultBean error(String code, String message){  
    	return new ResResultBean(false, code, message);  
    }  
    
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public String toString() {
		return "{\"code\":\""+code+"\",\"data\":"+JSONObject.toJSONString(data, SerializerFeature.WriteNullStringAsEmpty)+",\"message\":\""+message+"\",\"isSuccess\":"+isSuccess+"}";
	}
}
