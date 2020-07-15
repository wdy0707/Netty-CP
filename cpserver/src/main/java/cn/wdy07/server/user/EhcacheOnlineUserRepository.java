package cn.wdy07.server.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import io.netty.channel.Channel;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheOnlineUserRepository implements OnlineUserRepository {
	private static final int maxLoginClientCount = 5;
	private Cache cache = CacheManager.create("./src/main/resources/ehcache.xml").getCache("usercache");
	
	@Override
	public User getUser(String userId) {
		try {
			cache.acquireReadLockOnKey(userId);
			Element ele = cache.get(userId);
			if (ele != null)
				return (User) ele.getObjectValue();
			else
				return null;
		} finally {
			cache.releaseReadLockOnKey(userId);
		}
	}

	@Override
	public Client getClient(String userId, Channel channel) {
		try {
			cache.acquireReadLockOnKey(userId);
			Element ele = cache.get(userId);
			if (ele == null)
				return null;
			
			User user = (User) ele.getObjectValue();
			List<Client> clients;
			if (user == null || (clients = user.getOnlineClient()) == null || clients.size() == 0)
				return null;
			for (Client client : clients)
				if (client.getChannel().equals(channel))
					return client;
		} finally {
			cache.releaseReadLockOnKey(userId);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getUserIds() {
		return new HashSet<String>((List<String>) cache.getKeys());
	}

	@Override
	public void addClient(Client client) {
		String userId = client.getUserId();
		try {
			cache.acquireWriteLockOnKey(userId);
			Element ele = cache.get(userId);
			if (ele == null) {
				User user = new User();
				user.setUserId(userId);
				List<Client> onlineClient = new ArrayList<Client>();
				onlineClient.add(client);
				user.setOnlineClient(onlineClient);
				cache.put(new Element(userId, user));
			} else {
				User user = (User) ele.getObjectValue();
				List<Client> onlineClient = user.getOnlineClient();
				if (onlineClient.size() >= maxLoginClientCount)
					throw new ExceedMaxLoginClientException("超过最大登陆客户端个数： " + maxLoginClientCount);
				for (Client c : onlineClient) {
					if (c.getChannel().equals(client.getChannel()))
						throw new RepeatLoginException("该客户端已经登陆");

					if (c.getClientType().equals(client.getClientType()))
						throw new RepeatLoginException("同一类客户端不能登陆多个");
				}
				
				onlineClient.add(client);
			}
		} finally {
			cache.releaseWriteLockOnKey(userId);
		}
	}

	@Override
	public void deleteClient(String userId, Channel channel) {
		if (userId == null || channel == null)
			return;
		try {
			cache.acquireWriteLockOnKey(userId);
			Element ele = cache.get(userId);
			if (ele == null)
				return;
			
			User user = (User) ele.getObjectValue();
			Iterator<Client> iter = user.getOnlineClient().iterator();
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getChannel().equals(channel))
					iter.remove();
			}
			if (user.getOnlineClient().size() == 0)
				cache.remove(userId);
		} finally {
			cache.releaseWriteLockOnKey(userId);
		}
		
	}

	@Override
	public void deleteUser(String userId) {
		try {
			cache.acquireWriteLockOnKey(userId);
			cache.remove(userId);
		} finally {
			cache.releaseWriteLockOnKey(userId);
		}
	}

}
