package cn.wdy07.msgmodel;

public enum StatusSubType {
	TYPING(1),
	READ_RECEIPT_RESPONSE(2);
	
	private byte type;
	
	private StatusSubType(int type) {
		this.type = (byte) type;
	}
	
	public byte getType() {
		return type;
	}
}
