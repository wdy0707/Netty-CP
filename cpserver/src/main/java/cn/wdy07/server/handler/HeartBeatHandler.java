package cn.wdy07.server.handler;

import cn.wdy07.model.Message;
import cn.wdy07.server.client.Client;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author taylor
 *
 */
public class HeartBeatHandler implements MessageHandler {
	ClientManager manager = InMemeoryClientManager.getInstance();
	
	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		Client client = manager.getClient(message.getHeader().getUserId(), ctx.channel());
		client.clearHeartBeatCount();
		ctx.channel().writeAndFlush(message);
	}

}
