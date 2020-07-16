package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.BaseType;
import cn.wdy07.server.protocol.message.MessageWrapper;

public class MessageType2Qualifier extends SingleTypeQualifier {

	public MessageType2Qualifier(BaseType qualifiedType) {
		super(qualifiedType);
	}
	
	public MessageType2Qualifier(BaseType[] qualifiedType) {
		super(qualifiedType);
	}

	@Override
	protected BaseType getTypeInMessage(MessageWrapper wrapper) {
		return wrapper.getMessage().getHeader().getMessageType2();
	}
}
