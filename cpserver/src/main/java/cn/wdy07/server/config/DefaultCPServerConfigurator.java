package cn.wdy07.server.config;

import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.client.token.AllPassTokenChecker;
import cn.wdy07.server.client.token.SimpleToken;
import cn.wdy07.server.client.token.SimpleTokenConverter;
import cn.wdy07.server.client.token.Token;
import cn.wdy07.server.client.token.TokenCheckManager;
import cn.wdy07.server.handler.group.GroupManager;
import cn.wdy07.server.handler.group.SimpleLocalGroupManager;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.handler.persist.MessagePersistence;
import cn.wdy07.server.handler.persist.PrintConsoleMessagePersistence;
import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.handler.transfer.SimpleGroupMessageTransferer;
import cn.wdy07.server.handler.transfer.SimplePrivateMessageTransferer;

public class DefaultCPServerConfigurator implements CPServerConfigurator {

	@Override
	public TokenCheckManager<? extends Token> getTokenCheckManager() {
		return new TokenCheckManager<SimpleToken>(new AllPassTokenChecker(), new SimpleTokenConverter());
	}

	@Override
	public ClientManager getClientManager() {
		return InMemeoryClientManager.getInstance();
	}

	@Override
	public GroupManager getGroupManager() {
		return SimpleLocalGroupManager.getInstance();
	}

	@Override
	public OfflineMessageManager getOfflineMessageManager() {
		return InMemoryOfflineMessageManager.getInstance();
	}

	@Override
	public MessagePersistence getMessagePersistence() {
		return new PrintConsoleMessagePersistence();
	}

	@Override
	public MessageTransferer getPrivateMessageTransferer() {
		return new SimplePrivateMessageTransferer();
	}

	@Override
	public MessageTransferer getGroupMessageTransferer() {
		return new SimpleGroupMessageTransferer();
	}

}
