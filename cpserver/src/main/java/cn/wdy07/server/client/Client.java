package cn.wdy07.server.client;

import io.netty.channel.Channel;

public class Client {
	private String supportProtocol;
	private Channel clentChannel;

	public String getSupportProtocol() {
		return supportProtocol;
	}

	public void setSupportProtocol(String supportProtocol) {
		this.supportProtocol = supportProtocol;
	}

	public Channel getClentChannel() {
		return clentChannel;
	}

	public void setClentChannel(Channel clentChannel) {
		this.clentChannel = clentChannel;
	}

	
	public Client() {
	}

	public Client(String supportProtocol, Channel clentChannel) {
		this.supportProtocol = supportProtocol;
		this.clentChannel = clentChannel;
	}

}
