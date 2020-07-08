package cn.wdy07.server.handler;

import cn.wdy07.model.Message;
import cn.wdy07.server.handler.persist.MessagePersistence;
import cn.wdy07.server.handler.persist.PrintConsoleMessagePersistence;
import io.netty.channel.ChannelHandlerContext;

public class MessageStoreHandler implements MessageHandler {
	
	// 每个线程都有一个，并不一定必须要单例
	MessagePersistence persistence = new PrintConsoleMessagePersistence();
	
	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		persistence.persist(message);
	}

}
