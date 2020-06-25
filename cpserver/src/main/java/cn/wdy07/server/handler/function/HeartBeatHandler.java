package cn.wdy07.server.handler.function;

import cn.wdy07.msgmodel.Message;
import cn.wdy07.server.handler.MessageHandler;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatHandler implements MessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		ctx.writeAndFlush(message);
	}

}
