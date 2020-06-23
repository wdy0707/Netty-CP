package cn.wdy07.server.handler;

public class MessageHandlerNode {
	int convesationType;
	int messageType1;
	int messageTYpe2;
	MessageHandler hanlder;

	public MessageHandlerNode(int convesationType, int messageType1, int messageTYpe2, MessageHandler hanlder) {
		this.convesationType = convesationType;
		this.messageType1 = messageType1;
		this.messageTYpe2 = messageTYpe2;
		this.hanlder = hanlder;
	}

	public int getConvesationType() {
		return convesationType;
	}

	public void setConvesationType(int convesationType) {
		this.convesationType = convesationType;
	}

	public int getMessageType1() {
		return messageType1;
	}

	public void setMessageType1(int messageType1) {
		this.messageType1 = messageType1;
	}

	public int getMessageTYpe2() {
		return messageTYpe2;
	}

	public void setMessageTYpe2(int messageTYpe2) {
		this.messageTYpe2 = messageTYpe2;
	}

	public MessageHandler getHanlder() {
		return hanlder;
	}

	public void setHanlder(MessageHandler hanlder) {
		this.hanlder = hanlder;
	}
	
	
}