package cn.wdy07.server.handler.persist;

import cn.wdy07.model.Message;

public interface MessagePersistence {
	
	void persist(Message message);
}
