package cn.wdy07.server.client;

import java.util.List;

import cn.wdy07.model.header.ClientType;
import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.exception.UserUnLoggedInException;
import cn.wdy07.server.protocol.Protocol;
import io.netty.channel.Channel;

/**
 * 0 该接口的实现必须是单例的
 * 1 保存客户端信息，包括客户端的用户id，channel，支持的协议，客户端类型
 * 2 提供登入登出功能
 * 3 查询一个channel所支持的协议以便为该channel编码消息，若该channel支持多种协议，则根据Protocol的顺序返回最靠前的协议
 * 4 查询一个channel所对应的Client便于处理心跳报文
 * 5 心跳检测功能（因为该接口为客户端管理者，所以还是把心跳功能放在了这里，虽然不太符合单一职责原则）
 * 
 * @author taylor
 *
 */
public interface ClientManager {

	/**
	 * 获取该注册了的channel支持的协议，若该channel支持多种协议，则根据Protocol的顺序返回最靠前的协议
	 * 
	 * @param channel
	 * @return
	 */
	Protocol getSupportedProtocol(String userId, Channel channel) throws UserUnLoggedInException;
	
	Client getClient(String userId, Channel channel);

	/**
	 * 向ClientManager执行登陆操作
	 * 
	 * @param userId
	 * @param channel
	 * @param protocols 从LOGIN报文的content获取
	 * @param clientType header获取
	 */
	void login(String userId, Channel channel, List<Protocol> protocols, ClientType clientType)
			throws RepeatLoginException, ExceedMaxLoginClientException;
	
	/**
	 * 登出操作
	 * 
	 * @param userId
	 * @param channel
	 */
	void logout(String userId, Channel channel);
	
	
	
	void heartBeat();
}
