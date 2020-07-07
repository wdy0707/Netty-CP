package cn.wdy07.server.client;

import cn.wdy07.server.exception.UnsupportedProtocolException;
import cn.wdy07.server.protocol.Protocol;
import cn.wdy07.server.protocol.SupportedProtocol;
import io.netty.channel.Channel;

public abstract class AbstractClients implements Clients {

	@Override
	public void put(String userId, Channel channel, String clientProtocol, String clientType) {
		if (!SupportedProtocol.getInstance().support(clientProtocol))
			throw new UnsupportedProtocolException();
		
		Protocol protocol = null;
		for (Protocol p : Protocol.values())
			if (p.name().equals(clientProtocol))
				protocol = p;
		
		ClientType type = null;
		for (ClientType t : ClientType.values())
			if (t.name().equals(clientType))
				type = t;
		
		put(userId, channel, protocol, type);
	}

	abstract void put(String userId, Channel channel, Protocol protocol, ClientType clientType);
}
