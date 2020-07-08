package cn.wdy07.server.handler.persist;

import cn.wdy07.model.Message;

public class PrintConsoleMessagePersistence implements MessagePersistence {

	@Override
	public void persist(Message message) {
		System.out.println(message.getContent() + " stored");
	}

}
