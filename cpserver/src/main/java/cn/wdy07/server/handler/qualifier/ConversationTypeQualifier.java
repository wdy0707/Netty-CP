package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.Message;
import cn.wdy07.server.handler.MessageHandlerQualifier;

public class ConversationTypeQualifier extends SingleTypeQualifier implements MessageHandlerQualifier {
	
	public ConversationTypeQualifier(int qualifiedType) {
		super(qualifiedType);
	}
	
	public ConversationTypeQualifier(int[] qualifiedType) {
		super(qualifiedType);
	}

	@Override
	protected int getTypeInMessage(Message message) {
		return message.getHeader().getConversationType();
	}

}
