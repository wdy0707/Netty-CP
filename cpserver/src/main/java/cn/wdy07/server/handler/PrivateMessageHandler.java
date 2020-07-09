package cn.wdy07.server.handler;

import cn.wdy07.server.handler.transfer.MessageTransferer;
import cn.wdy07.server.handler.transfer.SimplePrivateMessageTransferer;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public class PrivateMessageHandler implements MessageHandler {
	private MessageTransferer transferer = new SimplePrivateMessageTransferer();
	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		transferer.transfer(wrapper);
	}


}
