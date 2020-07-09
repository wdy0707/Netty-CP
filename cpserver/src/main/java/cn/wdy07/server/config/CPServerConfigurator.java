package cn.wdy07.server.config;

import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.token.Token;
import cn.wdy07.server.client.token.TokenCheckManager;
import cn.wdy07.server.handler.group.GroupManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.handler.persist.MessagePersistence;
import cn.wdy07.server.handler.transfer.MessageTransferer;

public interface CPServerConfigurator {
	TokenCheckManager<? extends Token> getTokenCheckManager();
	
	ClientManager getClientManager();
	
	GroupManager getGroupManager();
	
	OfflineMessageManager getOfflineMessageManager();
	
	MessagePersistence getMessagePersistence();
	
	MessageTransferer getPrivateMessageTransferer();
	
	MessageTransferer getGroupMessageTransferer();
}
