package cn.wdy07.server.client;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.exception.UnAuthorizedTokenException;
import cn.wdy07.server.exception.UserUnLoggedInException;
import cn.wdy07.server.protocol.Protocol;
import io.netty.channel.Channel;

/**
 * 0 该接口的实现必须是单例的
 * 1 保存客户端信息，包括客户端的用户id，channel，支持的协议，客户端类型
 * 2 提供登入登出功能，该登陆非应用服务器登陆
 * 3 查询一个channel所支持的协议以便为该channel编码消息，若该channel支持多种协议，则根据Protocol的顺序返回最靠前的协议
 * 4 查询一个channel所对应的Client便于处理心跳报文
 * 5 心跳检测功能（因为该接口为客户端管理接口，所以还是把心跳功能放在了这里，虽然不太符合单一职责原则）
 * 
 * @author taylor
 *
 */
public interface ClientManager {

	/**
	 * 获取该注册了的channel支持的协议，若该channel支持多种协议，则根据Protocol的顺序返回最靠前的协议
	 * 可以使用userId构建数据结构辅助查询
	 * 
	 * @param channel
	 * @return
	 */
	Protocol getSupportedProtocol(String userId, Channel channel);
	
	/**
	 * 收到心跳报文时需要使用，查询对应的client，置零heartBeatCount
	 * 
	 * @param userId
	 * @param channel
	 * @return
	 */
	Client getClient(String userId, Channel channel);
	
	/**
	 * 查询用户所有客户端
	 * 
	 * @param userId
	 * @param channel
	 * @return
	 */
	List<Client> getUserClients(String userId);
	
	/**
	 * 向ClientManager执行登陆操作
	 * 
	 * @param message 登陆报文
	 * @param channel
	 * @throws RepeatLoginException
	 * @throws ExceedMaxLoginClientException
	 */
	void login(Message message, Channel channel)
			throws RepeatLoginException, ExceedMaxLoginClientException, UnAuthorizedTokenException;
	
	/**
	 * 登出操作
	 * 
	 * @param message 登出报文
	 * @param channel
	 */
	void logout(Message message, Channel channel);
	
	/**
	 * 在实现类中需要启动一个单线程池来处理，隔一定时间对每个Client的heartBeatCount加1，该数大于一定值则已断线
	 */
	void heartBeat();
}
