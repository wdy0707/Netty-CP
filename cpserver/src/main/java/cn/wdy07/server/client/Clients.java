package cn.wdy07.server.client;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class Clients {
	private static ConcurrentHashMap<String, Channel> clients = new ConcurrentHashMap<String, Channel>();
	private static ConcurrentHashMap<Channel, String> supportProtocol = new ConcurrentHashMap<Channel, String>();
	
	public static void put(String name, Channel channel, String supportProtocol) {
		if (clients.contains(name)) {
			Channel ch = clients.get(name);
			try {
				ch.close().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		clients.put(name, channel);
		Clients.supportProtocol.put(channel, supportProtocol);
	}
	
	public static Channel getChannel(String name) {
		return clients.get(name);
	}
	
	public static String getProtocol(Channel channel) {
		return supportProtocol.get(channel);
	}
}
