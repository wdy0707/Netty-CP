package cn.wdy07.server.handler;

import cn.wdy07.model.Message;
import io.netty.channel.ChannelHandlerContext;

public class MessageStoreHandler implements MessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		System.out.println("message stored");
	}

}
