package cn.wdy07.server.client;

import cn.wdy07.server.protocol.Protocol;
import io.netty.channel.Channel;

/**
 * 提供客户端操作接口
 * @author taylor
 *
 */
public interface Clients {
	
	/*
	 * 客户端登陆时候，带着客户端类型和支持的协议
	 */
	void put(String userId, Channel channel, String clientProtocol, String clientType);
	
	User getUser(String userId);
	
	Protocol getSupportProtocol(Channel channel);
}
