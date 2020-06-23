package cn.wdy07.server.handler;

import cn.wdy07.model.Message;
import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler {
	void handle(ChannelHandlerContext ctx, Message message);
}
