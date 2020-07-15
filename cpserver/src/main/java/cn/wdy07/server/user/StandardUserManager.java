package cn.wdy07.server.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.model.Message;
import cn.wdy07.model.Protocol;
import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.model.header.ClientType;
import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.exception.UnAuthorizedTokenException;
import cn.wdy07.server.protocol.message.MessageWrapper;
import cn.wdy07.server.user.token.Token;
import cn.wdy07.server.user.token.TokenCheckManager;
import io.netty.channel.Channel;

public class StandardUserManager implements UserManager {
	private TokenCheckManager<? extends Token> checkManager = CPServerContext.getContext().getConfigurator()
			.getTokenCheckManager();
	private OnlineUserRepository onlineUserRepository = CPServerContext.getContext().getConfigurator()
			.getOnlineUserRepository();
	private static final Logger logger = LoggerFactory.getLogger(StandardUserManager.class);
	@Override
	public void login(MessageWrapper wrapper, Channel channel) {
		Message message = wrapper.getMessage();
		String userId = message.getHeader().getUserId();
		try {
			
			// check token
			if (!checkManager.check(message))
				throw new UnAuthorizedTokenException();

			Client client = new Client();
			client.setUserId(userId);
			client.setChannel(channel);
			List<Protocol> protocols = ((LoginRequestMessageContent) message.getContent()).getSupportedProtocols();
			if (protocols.size() <= 0)
				throw new IllegalArgumentException("客户端未提供支持的协议，至少提供一个支持的协议");
			ArrayList<Protocol> p = new ArrayList<Protocol>(protocols);
			p.trimToSize();
			client.setProtocols(p);
			ClientType type = ((LoginRequestMessageContent) message.getContent()).getClientType();
			client.setClientType(type);
			
			onlineUserRepository.addClient(client);
			logger.info("{} login success", userId);
		} catch (Exception e) {
			logger.info("{} login failed with exception {}", userId, e);
			throw e;
		}
	}

	@Override
	public void logout(MessageWrapper wrapper, Channel channel) {
		Message message = wrapper.getMessage();
		String userId = message.getHeader().getUserId();
		try {
			channel.close();
			onlineUserRepository.deleteClient(userId, channel);
			logger.info("{} logout success", userId);
		} catch (Exception e) {
			logger.info("{} logout failed with exception {}", userId, e);
		}
	}

	@Override
	public User getUser(String userId) {
		return onlineUserRepository.getUser(userId);
	}

	@Override
	public Client getClient(String userId, Channel channel) {
		return onlineUserRepository.getClient(userId, channel);
	}

	@Override
	public Set<String> getUserIds() {
		return onlineUserRepository.getUserIds();
	}

	@Override
	public void addClient(Client client) {
		onlineUserRepository.addClient(client);
	}

	@Override
	public void deleteClient(String userId, Channel channel) {
		onlineUserRepository.deleteClient(userId, channel);
	}

	@Override
	public void deleteUser(String userId) {
		onlineUserRepository.deleteUser(userId);
	}

}
