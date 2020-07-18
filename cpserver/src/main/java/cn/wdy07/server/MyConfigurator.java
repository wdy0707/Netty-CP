package cn.wdy07.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.server.config.DefaultCPServerConfigurator;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.handler.offline.RedisOfflineMessageManager;
import cn.wdy07.server.user.OnlineUserRepository;
import cn.wdy07.server.user.ehcache.EhcacheOnlineUserRepository;

public class MyConfigurator extends DefaultCPServerConfigurator {
	private static final Logger logger = LoggerFactory.getLogger(MyConfigurator.class);

	@Override
	public OnlineUserRepository getOnlineUserRepository() {
		if (onlineUserRepository == null) {
			synchronized (this) {
				if (onlineUserRepository == null) {
					onlineUserRepository = new EhcacheOnlineUserRepository();
					logger.info("onlineUserRepository: {}", EhcacheOnlineUserRepository.class.getSimpleName());
				}
			}
		}
		return onlineUserRepository;
	}
	
	@Override
	public OfflineMessageManager getPrivateOfflineMessageManager() {
		if (privateOfflineMessageManager == null) {
			synchronized (this) {
				if (privateOfflineMessageManager == null)
					try {
						privateOfflineMessageManager = RedisOfflineMessageManager.getPrivateOfflineMessageManager();
						logger.info("privateOfflineMessageManager: {}", RedisOfflineMessageManager.class.getSimpleName());
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return privateOfflineMessageManager;
	}
	

	@Override
	public OfflineMessageManager getGroupOfflineMessageManager() {
		if (groupOfflineMessageManager == null) {
			synchronized (this) {
				if (groupOfflineMessageManager == null)
					try {
						groupOfflineMessageManager = RedisOfflineMessageManager.getGroupOfflineMessageManager();
						logger.info("groupOfflineMessageManager: {}", RedisOfflineMessageManager.class.getSimpleName());
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return groupOfflineMessageManager;
	}

}
