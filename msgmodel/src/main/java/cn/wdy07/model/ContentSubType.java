package cn.wdy07.model;

public enum ContentSubType {
	TEXT(0),
	VOICE(1),
	IMAGE(2),
	GIF(3),
	FILE(4),
	LOCATION(5),
	
	// 合并转发消息
	COMBINE(6);
	
	private byte type;
	
	private ContentSubType(int type) {
		this.type = (byte) type;
	}
	
	public byte getType() {
		return type;
	}
}
