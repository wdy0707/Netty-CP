package cn.wdy07.server.handler;

import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

//
public interface MessageHandler {
	void handle(ChannelHandlerContext ctx, MessageWrapper wrapper);
}
