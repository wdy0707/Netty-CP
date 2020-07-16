package cn.wdy07.server.handler;

import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.handler.persist.MessagePersistence;
import cn.wdy07.server.handler.persist.PrintConsoleMessagePersistence;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageStoreHandler implements MessageHandler {
	
	// 每个线程都有一个，并不一定必须要单例
	MessagePersistence persistence = CPServerContext.getContext().getConfigurator().getMessagePersistence();
	
	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		persistence.persist(wrapper);
	}

}
