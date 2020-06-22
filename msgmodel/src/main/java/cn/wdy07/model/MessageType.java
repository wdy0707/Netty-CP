package cn.wdy07.model;

public enum MessageType {
	// 内容类消息
	CONTENT(1),
	
	// 通知类消息
	NOTIFICATION(2),
	
	// 状态类消息
	STATUS(3),
	
	// 系统保留自用类
	SYSTEM(4);
	
	
	private byte type;
	private MessageType(int type) {
		this.type = (byte) type;
	}
	public byte getType() {
		return type;
	}

}
