package cn.wdy07.server.user;

import java.util.List;

import cn.wdy07.model.Protocol;
import cn.wdy07.model.header.ClientType;
import io.netty.channel.Channel;

public class Client {
	private String userId;
	private Channel channel;
	private ClientType clientType;
	private List<Protocol> protocols;

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

	public List<Protocol> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<Protocol> protocols) {
		this.protocols = protocols;
	}

	@Override
	public String toString() {
		return "Client [userId=" + userId + ", channel=" + channel + ", clientType=" + clientType + ", protocols="
				+ protocols + "]";
	}

}
