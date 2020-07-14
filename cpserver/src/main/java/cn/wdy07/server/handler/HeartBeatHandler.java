package cn.wdy07.server.handler;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.protocol.message.MessageWrapper;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.UserManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author taylor
 *
 */
public class HeartBeatHandler implements MessageHandler {
	UserManager manager = CPServerContext.getContext().getConfigurator().getUserManager();
	
	
	{
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				
			}
		}, 2, 5, TimeUnit.MINUTES);
	}
	
	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		String userId = wrapper.getMessage().getHeader().getUserId();
		Client client = manager.getClient(userId, ctx.channel());
		
		wrapper.addDescription(MessageWrapper.receiverKey, userId);
//		wrapper.addDescription(MessageWrapper.protocolKey, client.getOneSupportProtocol());
		ctx.channel().writeAndFlush(wrapper);
	}

}
