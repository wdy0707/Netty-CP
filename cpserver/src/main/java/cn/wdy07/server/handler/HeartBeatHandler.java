package cn.wdy07.server.handler;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.protocol.SupportedProtocol;
import cn.wdy07.server.protocol.message.MessageWrapper;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.User;
import cn.wdy07.server.user.UserManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author taylor
 *
 */
public class HeartBeatHandler implements MessageHandler {
	private UserManager manager = CPServerContext.getContext().getConfigurator().getUserManager();
	private SupportedProtocol supportedProtocol = CPServerContext.getContext().getConfigurator().getSupportedProtocol();
	private static final int maxHeartbeatCount = 5;
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

	{
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				for (String userId : manager.getUserIds()) {
					User user = manager.getUser(userId);
					synchronized (user) {
						if (user.getOnlineClient() != null && user.getOnlineClient().size() > 0) {
							Iterator<Client> iter = user.getOnlineClient().iterator();
							while (iter.hasNext()) {
								Client client = iter.next();
								if (maxHeartbeatCount <= client.addHeartbeatAndGet()) {
									try {
										client.getChannel().close();
									} catch (Exception e) {
									} finally {
										logger.info("receive no heartbeat from {}/{} and channel closed", userId,
												client.getClientType());
										iter.remove();
									}
								}
							}
						}
						if (user.getOnlineClient() == null || user.getOnlineClient().size() == 0)
							manager.deleteUser(userId);
					}
				}
			}
		}, 2, 5, TimeUnit.MINUTES);
		logger.info("heartbeat thread started.");
	}

	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		String userId = wrapper.getMessage().getHeader().getUserId();
		Client client = manager.getClient(userId, ctx.channel());

		wrapper.addDescription(MessageWrapper.receiverKey, userId);
		wrapper.addDescription(MessageWrapper.protocolKey,
				supportedProtocol.getOneSupportedProtocol(client.getProtocols()));
		ctx.channel().writeAndFlush(wrapper);
	}

}
