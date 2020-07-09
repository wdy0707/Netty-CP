package cn.wdy07.server.handler;

import cn.wdy07.server.client.Client;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author taylor
 *
 */
public class HeartBeatHandler implements MessageHandler {
	ClientManager manager = InMemeoryClientManager.getInstance();
	
	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		String userId = wrapper.getMessage().getHeader().getUserId();
		Client client = manager.getClient(userId, ctx.channel());
		client.clearHeartBeatCount();
		
		wrapper.addDescription(MessageWrapper.receiverKey, userId);
		wrapper.addDescription(MessageWrapper.protocolKey, client.getOneSupportProtocol());
		ctx.channel().writeAndFlush(wrapper);
	}

}
