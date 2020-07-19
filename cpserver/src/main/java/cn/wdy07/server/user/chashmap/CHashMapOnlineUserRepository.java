package cn.wdy07.server.user.chashmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.OnlineUserRepository;
import cn.wdy07.server.user.User;
import io.netty.channel.Channel;

public class CHashMapOnlineUserRepository implements OnlineUserRepository {
	private static final Logger logger = LoggerFactory.getLogger(CHashMapOnlineUserRepository.class);
	private ConcurrentHashMap<String, User> clientsMap = new ConcurrentHashMap<>();
	private static final int maxLoginClientCount = 5;

	@Override
	public User getUser(String userId) {
		return clientsMap.get(userId);
	}

	@Override
	public Set<String> getUserIds() {
		return clientsMap.keySet();
	}

	@Override
	public void addClient(Client client) {
		String userId = client.getUserId();
		User user = getUser(userId);
		// 插入一个空的User
		if (user == null) {
			User newUser = new User();
			newUser.setUserId(userId);
			newUser.setOnlineClient(new ArrayList<Client>());
			clientsMap.putIfAbsent(userId, newUser);
		}
		user = getUser(userId);

		synchronized (user) {
			try {
				List<Client> onlineClients = user.getOnlineClient();
				onlineClients.add(client);
			} catch (Exception e) {
				if (user.getOnlineClient().size() == 0)
					clientsMap.remove(userId);
				throw e;
			}
		}
	}

	// 使用userId 和 Channel来删除
	// 确保该client不存在即可
	@Override
	public void deleteClient(String userId, Channel channel) {
		if (userId == null || channel == null)
			return;

		User user = getUser(userId);
		if (user == null || user.getOnlineClient().size() == 0)
			return;
		
		synchronized (user) {
			List<Client> clients = user.getOnlineClient();
			Iterator<Client> iter = clients.iterator();
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getChannel().equals(channel))
					iter.remove();
			}
			if (clients.size() == 0)
				clientsMap.remove(userId);
		}
	}

	@Override
	public Client getClient(String userId, Channel channel) {
		if (userId == null || channel == null)
			return null;
		
		User user = getUser(userId);
		if (user == null || user.getOnlineClient().size() == 0)
			return null;
		synchronized (user) {
			List<Client> clients = user.getOnlineClient();
			for (Client c : clients)
				if (c.getChannel().equals(channel))
					return c;
		}
		return null;
	}

	@Override
	public void deleteUser(String userId) {
		clientsMap.remove(userId);
	}

}
