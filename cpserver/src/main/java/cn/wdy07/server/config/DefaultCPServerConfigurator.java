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
import cn.wdy07.server.user.OnlineUserRepository;
import cn.wdy07.server.user.StandardUserManager;
import cn.wdy07.server.user.UserManager;
import cn.wdy07.server.user.chashmap.CHashMapOnlineUserRepository;
import cn.wdy07.server.user.token.AllPassTokenChecker;
import cn.wdy07.server.user.token.SimpleToken;
import cn.wdy07.server.user.token.SimpleTokenConverter;
import cn.wdy07.server.user.token.Token;
import cn.wdy07.server.user.token.TokenCheckManager;

public class DefaultCPServerConfigurator implements CPServerConfigurator {

	protected UserManager userManager;
	protected OnlineUserRepository onlineUserRepository;
	protected SupportedProtocol supportedProtocol;
	protected GroupManager groupManager;
	protected OfflineMessageManager offlineMessageManager;
	protected TokenCheckManager<? extends Token> checkManager;
	protected MessagePersistence messagePersistence;
	protected MessageTransferer groupMessageTransferer;
	protected MessageTransferer privateMessageTransferer;
	
	@Override
	public TokenCheckManager<? extends Token> getTokenCheckManager() {
		if (checkManager == null) {
			synchronized (this) {
				if (checkManager == null)
					checkManager = new TokenCheckManager<SimpleToken>(new AllPassTokenChecker(),
							new SimpleTokenConverter());
			}
		}
		return checkManager;
	}

	@Override
	public GroupManager getGroupManager() {
		if (groupManager == null) {
			synchronized (this) {
				if (groupManager == null)
					groupManager = new SimpleLocalGroupManager();
			}
		}
		return groupManager;
	}

	@Override
	public OfflineMessageManager getOfflineMessageManager() {
		if (offlineMessageManager == null) {
			synchronized (this) {
				if (offlineMessageManager == null)
					offlineMessageManager = new InMemoryOfflineMessageManager();
			}
		}
		return offlineMessageManager;
	}

	@Override
	public MessagePersistence getMessagePersistence() {
		if (messagePersistence == null) {
			synchronized (this) {
				if (messagePersistence == null)
					messagePersistence = new PrintConsoleMessagePersistence();
			}
		}
		return messagePersistence;
	}

	@Override
	public MessageTransferer getPrivateMessageTransferer() {
		if (groupMessageTransferer == null) {
			synchronized (this) {
				if (groupMessageTransferer == null)
					groupMessageTransferer = new SimplePrivateMessageTransferer();
			}
		}
		return groupMessageTransferer;
	}

	@Override
	public MessageTransferer getGroupMessageTransferer() {
		if (groupMessageTransferer == null) {
			synchronized (this) {
				if (groupMessageTransferer == null)
					groupMessageTransferer = new SimpleGroupMessageTransferer();
			}
		}
		return groupMessageTransferer;
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
