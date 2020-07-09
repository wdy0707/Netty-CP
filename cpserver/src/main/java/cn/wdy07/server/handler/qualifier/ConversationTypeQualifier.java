package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.BaseType;
import cn.wdy07.server.protocol.message.MessageWrapper;

public class ConversationTypeQualifier extends SingleTypeQualifier implements MessageHandlerQualifier {
	
	public ConversationTypeQualifier(BaseType qualifiedType) {
		super(qualifiedType);
	}
	
	public ConversationTypeQualifier(BaseType[] qualifiedType) {
		super(qualifiedType);
	}

	@Override
	protected BaseType getTypeInMessage(MessageWrapper wrapper) {
		return wrapper.getMessage().getHeader().getConversationType();
	}

}
