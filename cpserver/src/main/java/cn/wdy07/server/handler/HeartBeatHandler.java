package cn.wdy07.server.handler;

import cn.wdy07.model.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author taylor
 *
 */
public class HeartBeatHandler implements MessageHandler {

	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		message.getHeader();
	}

}
