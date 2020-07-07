package cn.wdy07.server.client;

import io.netty.channel.Channel;

/**
 * 代表一个客户端，一个用户可以同时登陆多个客户端
 * 
 * @author taylor
 *
 */

public class Client {
	private String supportProtocol;
	private Channel channel;
	private ClientType clientType;

	/**
	 * 心跳计数，实现功能：连续多次未收到心跳则认为客户端已断线，服务端可以清理该客户端连接。实现上，服务端有一个线程定时对该数加1，
	 * 收到心跳消息时置零该数，该数大于一定值则已断线
	 */
	private int heartBeatCount;

	public String getSupportProtocol() {
		return supportProtocol;
	}

	public void setSupportProtocol(String supportProtocol) {
		this.supportProtocol = supportProtocol;
	}

	public Client() {
	}

	public Client(String supportProtocol, Channel channel) {
		this.supportProtocol = supportProtocol;
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		
		this.channel = channel;
	}

	public int addHeartBeatCountAndGet() {
		return ++heartBeatCount;
	}
	
	public void clearHeartBeatCount() {
		heartBeatCount++;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
}
