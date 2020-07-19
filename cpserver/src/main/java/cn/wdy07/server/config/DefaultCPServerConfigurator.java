package cn.wdy07.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import cn.wdy07.server.user.login.LoginStrategy;
import cn.wdy07.server.user.login.MultiClientLoginStrategy;
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
	protected OfflineMessageManager privateOfflineMessageManager;
	protected OfflineMessageManager groupOfflineMessageManager;
	protected TokenCheckManager<? extends Token> checkManager;
	protected MessagePersistence messagePersistence;
	protected MessageTransferer groupMessageTransferer;
	protected MessageTransferer privateMessageTransferer;
	protected LoginStrategy loginStrategy;
	
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultCPServerConfigurator.class);
	@Override
	public TokenCheckManager<? extends Token> getTokenCheckManager() {
		if (checkManager == null) {
			synchronized (this) {
				if (checkManager == null) {
					checkManager = new TokenCheckManager<SimpleToken>(new AllPassTokenChecker(),
							new SimpleTokenConverter());
					logger.info("TockenChecker: {}", AllPassTokenChecker.class.getSimpleName());
				}
			}
		}
		return checkManager;
	}

	@Override
	public GroupManager getGroupManager() {
		if (groupManager == null) {
			synchronized (this) {
				if (groupManager == null) {
					groupManager = new SimpleLocalGroupManager();
					logger.info("groupManager: {}", SimpleLocalGroupManager.class.getSimpleName());
				}
			}
		}
		return groupManager;
	}

	@Override
	public OfflineMessageManager getPrivateOfflineMessageManager() {
		if (privateOfflineMessageManager == null) {
			synchronized (this) {
				if (privateOfflineMessageManager == null) {
					privateOfflineMessageManager = groupOfflineMessageManager = new InMemoryOfflineMessageManager();
					logger.info("privateOfflineMessageManager: {}", InMemoryOfflineMessageManager.class.getSimpleName());
					logger.info("groupOfflineMessageManager: {}", InMemoryOfflineMessageManager.class.getSimpleName());
				}
			}
		}
		return privateOfflineMessageManager;
	}
	

	@Override
	public OfflineMessageManager getGroupOfflineMessageManager() {
		if (groupOfflineMessageManager == null) {
			synchronized (this) {
				if (groupOfflineMessageManager == null) {
					privateOfflineMessageManager = groupOfflineMessageManager = new InMemoryOfflineMessageManager();
					logger.info("privateOfflineMessageManager: {}", InMemoryOfflineMessageManager.class.getSimpleName());
					logger.info("groupOfflineMessageManager: {}", InMemoryOfflineMessageManager.class.getSimpleName());
				}
			}
		}
		return groupOfflineMessageManager;
	}

	@Override
	public MessagePersistence getMessagePersistence() {
		if (messagePersistence == null) {
			synchronized (this) {
				if (messagePersistence == null) {
					messagePersistence = new PrintConsoleMessagePersistence();
					logger.info("messagePersistence: {}", PrintConsoleMessagePersistence.class.getSimpleName());
				}
			}
		}
		return messagePersistence;
	}

	@Override
	public MessageTransferer getPrivateMessageTransferer() {
		if (privateMessageTransferer == null) {
			synchronized (this) {
				if (privateMessageTransferer == null) {
					privateMessageTransferer = new SimplePrivateMessageTransferer();
					logger.info("privateMessageTransferer: {}", SimplePrivateMessageTransferer.class.getSimpleName());
				}
			}
		}
		return privateMessageTransferer;
	}

	@Override
	public MessageTransferer getGroupMessageTransferer() {
		if (groupMessageTransferer == null) {
			synchronized (this) {
				if (groupMessageTransferer == null) {
					groupMessageTransferer = new SimpleGroupMessageTransferer();
					logger.info("groupMessageTransferer: {}", SimpleGroupMessageTransferer.class.getSimpleName());
				}
			}
		}
		return groupMessageTransferer;
	}

	@Override
	public UserManager getUserManager() {
		if (userManager == null) {
			synchronized (this) {
				if (userManager == null) {
					userManager = new StandardUserManager();
					logger.info("userManager: {}", StandardUserManager.class.getSimpleName());
				}
			}
		}
		return userManager;
	}

	@Override
	public OnlineUserRepository getOnlineUserRepository() {
		if (onlineUserRepository == null) {
			synchronized (this) {
				if (onlineUserRepository == null) {
					onlineUserRepository = new CHashMapOnlineUserRepository();
					logger.info("onlineUserRepository: {}", CHashMapOnlineUserRepository.class.getSimpleName());
				}
			}
		}
		return onlineUserRepository;
	}

	@Override
	public SupportedProtocol getSupportedProtocol() {
		if (supportedProtocol == null) {
			synchronized (this) {
				if (supportedProtocol == null) {
					supportedProtocol = new SupportedProtocol();
					logger.info("supportedProtocol: {}", SupportedProtocol.class.getSimpleName());
				}
			}
		}
		return supportedProtocol;
	}

	@Override
	public LoginStrategy getLoginStrategy() {
		if (loginStrategy == null) {
			synchronized (this) {
				if (loginStrategy == null) {
					loginStrategy = new MultiClientLoginStrategy();
					logger.info("loginStrategy: {}", MultiClientLoginStrategy.class.getSimpleName());
				}
			}
		}
		return loginStrategy;
	}
}
