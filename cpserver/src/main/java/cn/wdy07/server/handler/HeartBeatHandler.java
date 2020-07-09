package cn.wdy07.server.handler;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.client.Client;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author taylor
 *
 */
public class HeartBeatHandler implements MessageHandler {
	ClientManager manager = CPServerContext.getContext().getConfigurator().getClientManager();
	
	
	{
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				manager.heartBeat();
			}
		}, 2, 5, TimeUnit.MINUTES);
	}
	
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
