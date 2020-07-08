package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.BaseType;
import cn.wdy07.model.Message;
import cn.wdy07.server.handler.MessageHandlerQualifier;

public class ConversationTypeQualifier extends SingleTypeQualifier implements MessageHandlerQualifier {
	
	public ConversationTypeQualifier(BaseType qualifiedType) {
		super(qualifiedType);
	}
	
	public ConversationTypeQualifier(BaseType[] qualifiedType) {
		super(qualifiedType);
	}

	@Override
	protected BaseType getTypeInMessage(Message message) {
		return message.getHeader().getConversationType();
	}

}
