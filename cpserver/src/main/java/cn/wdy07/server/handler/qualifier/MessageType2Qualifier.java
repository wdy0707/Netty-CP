package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.Message;

public class MessageType2Qualifier extends SingleTypeQualifier {

	public MessageType2Qualifier(int qualifiedType) {
		super(qualifiedType);
	}
	
	public MessageType2Qualifier(int[] qualifiedType) {
		super(qualifiedType);
	}

	@Override
	protected int getTypeInMessage(Message message) {
		return message.getHeader().getMessageType2();
	}
}
