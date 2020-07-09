package cn.wdy07.server.handler;

import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.handler.transfer.SimpleGroupMessageTransferer;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public class GroupMessageHandler implements MessageHandler {
	MessageTransferer transferer = CPServerContext.getContext().getConfigurator().getGroupMessageTransferer();;
	
	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		transferer.transfer(wrapper);
	}

}
