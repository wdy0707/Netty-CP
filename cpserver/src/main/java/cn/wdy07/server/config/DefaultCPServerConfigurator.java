package cn.wdy07.server.config;

import cn.wdy07.server.handler.group.GroupManager;
import cn.wdy07.server.handler.group.SimpleLocalGroupManager;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.handler.persist.MessagePersistence;
import cn.wdy07.server.handler.persist.PrintConsoleMessagePersistence;
import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.handler.transfer.SimpleGroupMessageTransferer;
import cn.wdy07.server.handler.transfer.SimplePrivateMessageTransferer;
import cn.wdy07.server.protocol.SupportedProtocol;
import cn.wdy07.server.user.CHashMapOnlineUserRepository;
import cn.wdy07.server.user.OnlineUserRepository;
import cn.wdy07.server.user.StandardUserManager;
import cn.wdy07.server.user.UserManager;
import cn.wdy07.server.user.token.AllPassTokenChecker;
import cn.wdy07.server.user.token.SimpleToken;
import cn.wdy07.server.user.token.SimpleTokenConverter;
import cn.wdy07.server.user.token.Token;
import cn.wdy07.server.user.token.TokenCheckManager;

public class DefaultCPServerConfigurator implements CPServerConfigurator {

	private UserManager userManager;
	private OnlineUserRepository onlineUserRepository;
	private SupportedProtocol supportedProtocol;
	@Override
	public TokenCheckManager<? extends Token> getTokenCheckManager() {
		return new TokenCheckManager<SimpleToken>(new AllPassTokenChecker(), new SimpleTokenConverter());
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

	@Override
	public UserManager getUserManager() {
		if (userManager == null) {
			synchronized (this) {
				if (userManager == null)
					userManager = new StandardUserManager();
			}
		}
		return userManager;
	}

	@Override
	public OnlineUserRepository getOnlineUserRepository() {
		if (onlineUserRepository == null) {
			synchronized (this) {
				if (onlineUserRepository == null)
					onlineUserRepository = new CHashMapOnlineUserRepository();
			}
		}
		return onlineUserRepository;
	}

	@Override
	public SupportedProtocol getSupportedProtocol() {
		if (supportedProtocol == null) {
			synchronized (this) {
				if (supportedProtocol == null)
					supportedProtocol = new SupportedProtocol();
			}
		}
		return supportedProtocol;
	}

}
