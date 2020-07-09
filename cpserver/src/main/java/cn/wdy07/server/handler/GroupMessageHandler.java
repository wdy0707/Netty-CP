package cn.wdy07.server.handler;

import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.handler.transfer.SimpleGroupMessageTransferer;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public class GroupMessageHandler implements MessageHandler {
	MessageTransferer transferer = new SimpleGroupMessageTransferer();
	
	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		transferer.transfer(wrapper);
	}

}
