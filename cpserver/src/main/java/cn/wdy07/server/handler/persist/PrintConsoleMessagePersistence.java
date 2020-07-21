package cn.wdy07.server.handler.persist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.server.protocol.message.MessageWrapper;

public class PrintConsoleMessagePersistence implements MessagePersistence {
	private static final Logger logger = LoggerFactory.getLogger(PrintConsoleMessagePersistence.class);
	@Override
	public void persist(MessageWrapper wrapper) {
		logger.info("{} stored", wrapper.getMessage());
	}

}
