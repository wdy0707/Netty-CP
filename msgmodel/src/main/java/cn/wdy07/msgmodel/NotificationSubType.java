package cn.wdy07.msgmodel;

public enum NotificationSubType {
	CONTACT(1),
	PROFILE(2),
	COMMAND(3),
	INFORMATION(4),
	READ_RECEIPT(5);
	
	private byte type;
	
	private NotificationSubType(int type) {
		this.type = (byte) type;
	}
	
	public byte getType() {
		return type;
	}
}
