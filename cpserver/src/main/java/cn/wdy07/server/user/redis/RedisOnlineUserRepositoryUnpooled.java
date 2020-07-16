package cn.wdy07.server.user.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.OnlineUserRepository;
import cn.wdy07.server.user.User;
import io.netty.channel.Channel;
import redis.clients.jedis.Jedis;

/**
 * channel似乎不可以序列化
 * @author jinzhiqiang
 *
 */
public class RedisOnlineUserRepositoryUnpooled implements OnlineUserRepository {
	private Jedis jedis = new Jedis("192.168.99.100", 6379);
	private static final String lockSuffix = "_lock";
	private static final int retryTimes = 3;
	private static final int retryTime = 1000;
	private static final int maxLoginClientCount = 5;

	private void lock(String userId) {
		int times = retryTimes;
		while (times-- > 0) {
			if (jedis.setnx(userId + lockSuffix, "") == 1)
				return;
			else
				try {
					Thread.sleep(retryTime);
				} catch (InterruptedException e) {
					throw new LockFailureException("interrupted when lock on " + userId);
				}
		}
		throw new LockFailureException();
	}

	private void unlock(String userId) {
		jedis.del(userId + lockSuffix);
	}

	@Override
	public User getUser(String userId) {
		try {
			lock(userId);
			String userStr = jedis.get(userId);
			if (userStr != null)
				return JSON.parseObject(userStr, User.class);
			else
				return null;
		} finally {
			unlock(userId);
		}
	}

	@Override
	public Client getClient(String userId, Channel channel) {
		try {
			lock(userId);
			String userStr = jedis.get(userId);
			if (userStr != null) {
				User user = JSON.parseObject(userStr, User.class);
				List<Client> clients;
				if (user == null || (clients = user.getOnlineClient()) == null || clients.size() == 0)
					return null;
				for (Client client : clients)
					if (client.getChannel().equals(channel))
						return client;
			}
		} finally {
			unlock(userId);
		}

		return null;
	}

	@Override
	public Set<String> getUserIds() {
		Set<String> set = jedis.keys("*");
		Iterator<String> iter = set.iterator();
		while (iter.hasNext())
			if (iter.next().endsWith(lockSuffix))
				iter.remove();
		return set;
	}

	@Override
	public void addClient(Client client) {
		String userId = client.getUserId();
		try {
			lock(userId);
			String userStr = jedis.get(userId);
			User user;
			if (userStr != null) {
				user = JSON.parseObject(userStr, User.class);
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
			} else {
				user = new User();
				user.setUserId(userId);
				List<Client> onlineClient = new ArrayList<Client>();
				onlineClient.add(client);
				user.setOnlineClient(onlineClient);
			}
			jedis.set(userId, JSON.toJSONString(user));
		} finally {
			unlock(userId);
		}
	}

	@Override
	public void deleteClient(String userId, Channel channel) {
		try {
			lock(userId);
			String userStr = jedis.get(userId);
			if (userStr != null) {
				User user = JSON.parseObject(userStr, User.class);
				List<Client> clients;
				if (user == null || (clients = user.getOnlineClient()) == null || clients.size() == 0)
					return;
				
				Iterator<Client> iter = user.getOnlineClient().iterator();
				while (iter.hasNext()) {
					Client c = iter.next();
					if (c.getChannel().equals(channel))
						iter.remove();
				}
				jedis.set(userId, JSON.toJSONString(user));
			}
		} finally {
			unlock(userId);
		}

	}

	@Override
	public void deleteUser(String userId) {
		try {
			lock(userId);
			jedis.del(userId);
		} finally {
			unlock(userId);
		}

	}

}
