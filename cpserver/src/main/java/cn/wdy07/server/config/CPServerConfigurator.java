package cn.wdy07.server.config;

import cn.wdy07.server.handler.group.GroupManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.handler.persist.MessagePersistence;
import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.protocol.SupportedProtocol;
import cn.wdy07.server.user.OnlineUserRepository;
import cn.wdy07.server.user.UserManager;
import cn.wdy07.server.user.login.LoginStrategy;
import cn.wdy07.server.user.token.Token;
import cn.wdy07.server.user.token.TokenCheckManager;

public interface CPServerConfigurator {
	TokenCheckManager<? extends Token> getTokenCheckManager();
	
	UserManager getUserManager();
	
	OnlineUserRepository getOnlineUserRepository();
	
	GroupManager getGroupManager();
	
	OfflineMessageManager getPrivateOfflineMessageManager();
	
	OfflineMessageManager getGroupOfflineMessageManager();
	
	MessagePersistence getMessagePersistence();
	
	MessageTransferer getPrivateMessageTransferer();
	
	MessageTransferer getGroupMessageTransferer();
	
	SupportedProtocol getSupportedProtocol();
	
	LoginStrategy getLoginStrategy();
}
