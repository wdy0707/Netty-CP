package cn.wdy07.server.user;

import cn.wdy07.model.header.ClientType;
import io.netty.channel.Channel;

public class Client {
	private String userId;
	private Channel channel;
	private ClientType clientType;

	private int heartbeat;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public int addHeartbeatAndGet() {
		return ++heartbeat;
	}

	public void setHeartbeatZero() {
		heartbeat = 0;
	}

	@Override
	public String toString() {
		return "Client [userId=" + userId + ", channel=" + channel + ", clientType=" + clientType + ", heartbeat="
				+ heartbeat + "]";
	}

}
