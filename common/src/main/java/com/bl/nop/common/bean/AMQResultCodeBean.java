package com.bl.nop.common.bean;

public class AMQResultCodeBean extends ResultCodeBean {

	public final static ResultCodeBean MSG_EMP = new AMQResultCodeBean("000001", "推送消息为空");
	
	public final static ResultCodeBean COMMAND_EMP = new AMQResultCodeBean("000002", "推送消息命令为空");
	
	public final static ResultCodeBean DATA_EMP = new AMQResultCodeBean("000003", "推送消息数据为空");
	
	public final static ResultCodeBean MSG_INVALID = new AMQResultCodeBean("000004", "推送消息格式无效");
	
	public final static ResultCodeBean PUSH_ERROR = new AMQResultCodeBean("000005", "推送消息失败");
	
	public final static ResultCodeBean QUEUE_EMP = new AMQResultCodeBean("000006", "队列为空");
	
	public final static ResultCodeBean QUEUE_ERROR = new AMQResultCodeBean("000007", "发送消息失败");
	
	
	public AMQResultCodeBean(String code, String message) {
		super(code, message);
	}

}
