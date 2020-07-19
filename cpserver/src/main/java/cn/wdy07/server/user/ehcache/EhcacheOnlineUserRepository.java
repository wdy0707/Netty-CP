package cn.wdy07.server.user.ehcache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.OnlineUserRepository;
import cn.wdy07.server.user.User;
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
			if (ele == null || ele.getObjectValue() == null) {
				User user = new User();
				user.setUserId(userId);
				List<Client> onlineClient = new ArrayList<Client>();
				onlineClient.add(client);
				user.setOnlineClient(onlineClient);
				cache.put(new Element(userId, user));
			} else {
				User user = (User) ele.getObjectValue();
				List<Client> onlineClient = user.getOnlineClient();
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
