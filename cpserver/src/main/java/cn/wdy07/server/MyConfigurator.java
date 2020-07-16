package cn.wdy07.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.server.config.DefaultCPServerConfigurator;
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
					logger.info("use {}", EhcacheOnlineUserRepository.class.getSimpleName());
				}
			}
		}
		return onlineUserRepository;
	}

}
