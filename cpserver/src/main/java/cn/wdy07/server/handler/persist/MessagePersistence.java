package cn.wdy07.server.handler.persist;

import cn.wdy07.server.protocol.message.MessageWrapper;

public interface MessagePersistence {
	
	void persist(MessageWrapper wrapper);
}
