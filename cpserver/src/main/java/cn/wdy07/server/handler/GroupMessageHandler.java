package cn.wdy07.server.handler;

import cn.wdy07.model.Message;
import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.handler.transfer.SimpleGroupMessageTransferer;
import io.netty.channel.ChannelHandlerContext;

public class GroupMessageHandler implements MessageHandler {
	MessageTransferer transferer = new SimpleGroupMessageTransferer();
	
	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		transferer.transfer(message);
	}

}
