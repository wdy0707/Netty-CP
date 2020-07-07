package cn.wdy07.server.handler;

public class MessageHandlerNode {
	private MessageHandlerQualifier qualifier;
	private MessageHandler hanlder;

	public MessageHandlerNode() {

	}

	public MessageHandlerNode(MessageHandlerQualifier qualifier, MessageHandler handler) {
		this.setQualifier(qualifier);
		this.hanlder = handler;
	}

	public MessageHandler getHanlder() {
		return hanlder;
	}

	public void setHanlder(MessageHandler hanlder) {
		this.hanlder = hanlder;
	}

	public MessageHandlerQualifier getQualifier() {
		return qualifier;
	}

	public void setQualifier(MessageHandlerQualifier qualifier) {
		this.qualifier = qualifier;
	}

}