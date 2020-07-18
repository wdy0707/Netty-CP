package cn.wdy07.server.handler.offline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.protocol.message.MessageWrapper;
import cn.wdy07.server.user.UserManager;
import redis.clients.jedis.Jedis;

public class RedisOfflineMessageManager implements OfflineMessageManager {
	private static final String privateManagerName = "p";
	private static final String groupManagerName = "g";
	
	private UserManager userManager = CPServerContext.getContext().getConfigurator().getUserManager();
	ThreadLocal<Jedis> localJedis = ThreadLocal.withInitial(() -> new Jedis("192.168.99.100", 6379));
	
	// 作为群消息、私聊消息的key的后缀
	private String suffix;
	
	private RedisOfflineMessageManager(String name) {
		this.suffix = "_" + name;
	}
	
	public static RedisOfflineMessageManager getPrivateOfflineMessageManager() throws IOException {
		return new RedisOfflineMessageManager(privateManagerName);
	}
	
	public static RedisOfflineMessageManager getGroupOfflineMessageManager() throws IOException {
		return new RedisOfflineMessageManager(groupManagerName);
	}

	@Override
	public void putOfflineMessage(String userId, MessageWrapper wrapper) {
		if (userManager.getUser(userId) != null)
			throw new IllegalStateException("user has logined.");
		
		Jedis jedis = localJedis.get();
		jedis.rpush(userId + suffix, JSON.toJSONString(wrapper));
	}

	@Override
	public List<MessageWrapper> getOfflineMessage(String userId) {
		if (userManager.getUser(userId) == null)
			throw new IllegalStateException("user hasnot logined.");
		
		List<String> strings = localJedis.get().lrange(userId + suffix, 0, -1);
		List<MessageWrapper> wrappers = new ArrayList<MessageWrapper>(strings.size());
		for (int i = 0; i < strings.size(); i++) {
			wrappers.add(JSON.parseObject(strings.get(i), MessageWrapper.class));
		}
		return wrappers;
	}

}
