package cn.wdy07.server.handler.qualifier;

import cn.wdy07.server.protocol.message.MessageWrapper;

public class HandlerAllQualifier implements MessageHandlerQualifier {
	
	public static HandlerAllQualifier handlerAllQualifier = new HandlerAllQualifier();
	
	private HandlerAllQualifier() {
	}
	@Override
	public boolean qualify(MessageWrapper wrapper) {
		return true;
	}
}
