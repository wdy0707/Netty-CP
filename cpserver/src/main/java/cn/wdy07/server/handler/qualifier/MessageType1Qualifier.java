package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.Message;

public class MessageType1Qualifier extends SingleTypeQualifier {

	public MessageType1Qualifier(int qualifiedType) {
		super(qualifiedType);
	}
	
	public MessageType1Qualifier(int[] qualifiedType) {
		super(qualifiedType);
	}

	@Override
	protected int getTypeInMessage(Message message) {
		return message.getHeader().getMessageType1();
	}
}
