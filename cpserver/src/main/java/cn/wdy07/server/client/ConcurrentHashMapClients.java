package cn.wdy07.server.client;

import java.util.concurrent.ConcurrentHashMap;

import cn.wdy07.server.protocol.Protocol;
import io.netty.channel.Channel;

public class ConcurrentHashMapClients extends AbstractClients {
	private static ConcurrentHashMapClients concurrentHashMapClients = new ConcurrentHashMapClients();
	
	
	private ConcurrentHashMap<String, Channel> clients = new ConcurrentHashMap<String, Channel>();
	private ConcurrentHashMap<Channel, String> supportProtocol = new ConcurrentHashMap<Channel, String>();
	private ConcurrentHashMapClients() {

	}
	
	public static ConcurrentHashMapClients getInstance() {
		return concurrentHashMapClients;
	}

	@Override
	public User getUser(String userId) {
		return null;
	}

	@Override
	public Protocol getSupportProtocol(Channel channel) {
		return null;
	}

	@Override
	void put(String userId, Channel channel, Protocol protocol, ClientType clientType) {
		
	}

}
