package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.Message;
import cn.wdy07.server.handler.MessageHandlerQualifier;

public class HandlerAllQualifier implements MessageHandlerQualifier {
	
	public static HandlerAllQualifier handlerAllQualifier = new HandlerAllQualifier();
	
	private HandlerAllQualifier() {
	}
	@Override
	public boolean qualify(Message message) {
		return true;
	}
}
