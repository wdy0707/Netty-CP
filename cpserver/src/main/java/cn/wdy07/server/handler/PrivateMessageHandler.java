package cn.wdy07.server.handler;

import cn.wdy07.model.Message;
import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.handler.transfer.SimplePrivateMessageTransferer;
import io.netty.channel.ChannelHandlerContext;

public class PrivateMessageHandler implements MessageHandler {
	private MessageTransferer transferer = new SimplePrivateMessageTransferer();
	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		transferer.transfer(message);
	}


}
