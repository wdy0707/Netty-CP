package cn.wdy07.server.handler.persist;

import cn.wdy07.server.protocol.message.MessageWrapper;

public class PrintConsoleMessagePersistence implements MessagePersistence {

	@Override
	public void persist(MessageWrapper wrapper) {
		System.out.println(wrapper.getMessage() + " stored");
	}

}
