package cn.wdy07.server.client;

import java.util.ArrayList;
import java.util.List;

import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import io.netty.channel.Channel;

// 封装一层User，控制最大同时在线客户端个数并及时使用trimToSize减少内存使用
public class User {
	// 一个用户最大同时在线客户端个数
	public static final int MAX_ONLINE_COUNT = 5;
	
	// 及时使用trimToSize减少内存使用
	private ArrayList<Client> clients = new ArrayList<Client>();

	public void addClient(Client client) {
		if (channelExist(client.getChannel()))
			throw new RepeatLoginException("客户端已登陆");
		
		if (clients.size() >= 5)
			throw new ExceedMaxLoginClientException("超过最大同时在线客户端数：" + MAX_ONLINE_COUNT);
		
		clients.add(client);
		clients.trimToSize();
	}
	
	private boolean channelExist(Channel ch) {
		for (Client client : clients) {
			if (ch.equals(client.getChannel()))
				return true;
		}
		
		return false;
	}
}
