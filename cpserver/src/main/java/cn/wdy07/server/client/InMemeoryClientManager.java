package cn.wdy07.server.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.wdy07.model.Message;
import cn.wdy07.model.content.LoginMessageContent;
import cn.wdy07.model.header.ClientType;
import cn.wdy07.server.client.token.AllPassTokenChecker;
import cn.wdy07.server.client.token.SimpleToken;
import cn.wdy07.server.client.token.SimpleTokenConverter;
import cn.wdy07.server.client.token.TokenCheckManager;
import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.exception.UnAuthorizedTokenException;
import cn.wdy07.server.exception.UserUnLoggedInException;
import cn.wdy07.server.protocol.Protocol;
import io.netty.channel.Channel;

public class InMemeoryClientManager implements ClientManager {

	/*
	 * 使用arraylist保存客户端，及时调用trimToSize节省内存使用
	 */
	private ConcurrentHashMap<String, ArrayList<Client>> clientsMap = new ConcurrentHashMap<String, ArrayList<Client>>();
	private int maxLoginClientCount = 5;

	private TokenCheckManager<SimpleToken> checkManager = new TokenCheckManager<SimpleToken>(new AllPassTokenChecker(),
			new SimpleTokenConverter());

	private InMemeoryClientManager() {

	}

	private static InMemeoryClientManager inMemeoryClientManager = new InMemeoryClientManager();

	public static InMemeoryClientManager getInstance() {
		return inMemeoryClientManager;
	}

	@Override
	public Protocol getSupportedProtocol(String userId, Channel channel) {
		ArrayList<Client> clients = clientsMap.get(userId);
		if (clients == null || clients.isEmpty()) // 没有登录
			throw new UserUnLoggedInException("用户未登录");

		synchronized (clients) {
			Protocol protocol = null;
			for (Client client : clients)
				if (channel.equals(client.getChannel())) {
					// 登陆时已经check过，至少包含一个
					List<Protocol> protocols = client.getProtocols();

					// 查找ordinal最小的
					protocol = protocols.get(0);
					for (int i = 1; i < protocols.size(); i++) {
						Protocol p = protocols.get(i);
						if (protocol.ordinal() > p.ordinal())
							protocol = p;
					}
				}

			return protocol;
		}
	}

	@Override
	public Client getClient(String userId, Channel channel) {
		ArrayList<Client> clients = clientsMap.get(userId);
		if (clients == null || clients.isEmpty()) // 没有登录
			return null;

		synchronized (clients) {
			for (Client client : clients)
				if (channel.equals(client.getChannel()))
					return client;

			return null;
		}
	}

	@Override
	public List<Client> getUserClients(String userId) {
		return clientsMap.get(userId);
	}

	/*
	 * 需要保证同一个用户不同客户端、不同用户登陆时的线程安全 (non-Javadoc)
	 * 
	 * @see cn.wdy07.server.client.ClientManager#login(cn.wdy07.model.Message,
	 * io.netty.channel.Channel)
	 */
	@Override
	public void login(Message message, Channel channel)
			throws RepeatLoginException, ExceedMaxLoginClientException, UnAuthorizedTokenException {
		String userId = message.getHeader().getUserId();
		ArrayList<Client> clients = clientsMap.putIfAbsent(userId, new ArrayList<Client>());
		if (clients == null) // 原先没有任何客户端登陆
			clients = clientsMap.get(userId);
		// else 已经有客户端登陆，putIfAbsent返回了原先的值

		synchronized (clients) {
			// check login clients count
			if (clients.size() >= maxLoginClientCount)
				throw new ExceedMaxLoginClientException("超过最大登陆客户端个数： " + maxLoginClientCount);

			// check repeatLogin
			ClientType type = message.getHeader().getClientType();
			for (Client loginedClient : clients) {
				if (channel.equals(loginedClient.getChannel()))
					throw new RepeatLoginException("该客户端已经登陆");

				if (type == loginedClient.getClientType())
					throw new RepeatLoginException("同一类客户端不能登陆多个");
			}

			// check token
			if (!checkManager.check(message))
				throw new UnAuthorizedTokenException();

			// OK
			Client client = new Client();
			client.setChannel(channel);
			List<Integer> protocolIntegers = ((LoginMessageContent) message.getContent()).getSupportedProtocols();
			if (protocolIntegers.size() <= 0)
				throw new IllegalArgumentException("客户端未提供支持的协议，至少提供一个支持的协议");

			ArrayList<Protocol> protocols = new ArrayList<Protocol>(protocolIntegers.size());
			for (int i : protocolIntegers) {
				if (i >= Protocol.values().length)
					throw new IllegalArgumentException("错误的协议类型：" + i);
				protocols.add(Protocol.values()[i]);
			}

			protocols.trimToSize();
			client.setProtocols(protocols);
			client.setClientType(type);
			clients.add(client);
			clients.trimToSize();
		}
	}

	@Override
	public void logout(Message message, Channel channel) {
		ArrayList<Client> clients = clientsMap.get(message.getHeader().getUserId());
		if (clients == null || clients.isEmpty()) // 没有登录
			return;

		synchronized (clients) {
			Iterator<Client> iter = clients.iterator();
			while (iter.hasNext()) {
				Client client = iter.next();
				if (channel.equals(client.getChannel()))
					iter.remove();
			}
			clients.trimToSize();
		}
	}

	@Override
	public void heartBeat() {
		clientsMap.forEach((k, v) -> {
			synchronized (v) {
				Iterator<Client> iter = v.iterator();
				for (; iter.hasNext(); ) {
					Client client = iter.next();
					if (client.addHeartBeatCountAndGet() >= Client.maxHeartBeatCount) {
						iter.remove();
					}
				}
				v.trimToSize();
			}
		});
	}

}