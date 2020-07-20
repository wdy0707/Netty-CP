package cn.wdy07.server.user;

import java.util.List;

import cn.wdy07.model.header.ClientType;
import io.netty.channel.Channel;

public class ClientBuilder {
	private Client client;
	
	public static ClientBuilder create() {
		return new ClientBuilder();
	}
	
	private ClientBuilder() {
		client = new Client();
	}
	
	public ClientBuilder userId(String userId) {
		client.setUserId(userId);
		return this;
	}
	
	public ClientBuilder channel(Channel channel) {
		client.setChannel(channel);
		return this;
	}
	
	public ClientBuilder clientType(ClientType clientType) {
		client.setClientType(clientType);
		return this;
	}
	
	public Client build() {
		return client;
	}
}
