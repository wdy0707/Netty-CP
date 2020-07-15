package cn.wdy07.server.handler.transfer;

import cn.wdy07.model.Message;
import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.protocol.SupportedProtocol;
import cn.wdy07.server.protocol.message.MessageWrapper;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.User;
import cn.wdy07.server.user.UserManager;

public class SimplePrivateMessageTransferer implements MessageTransferer {
	private OfflineMessageManager offlineMessageManager = CPServerContext.getContext().getConfigurator()
			.getOfflineMessageManager();
	private UserManager userManager = CPServerContext.getContext().getConfigurator().getUserManager();
	private SupportedProtocol supportedProtocol = CPServerContext.getContext().getConfigurator().getSupportedProtocol();

	@Override
	public void transfer(MessageWrapper wrapper) {
		Message message = wrapper.getMessage();
		String userId = message.getHeader().getUserId();
		String targetId = message.getHeader().getTargetId();

		User from = userManager.getUser(userId);
		User to = userManager.getUser(targetId);

		MessageWrapper outWrapper;
		for (Client client : from.getOnlineClient()) {
			outWrapper = new MessageWrapper(wrapper);
			outWrapper.addDescription(MessageWrapper.receiverKey, userId);
			outWrapper.addDescription(MessageWrapper.protocolKey,
					supportedProtocol.getOneSupportedProtocol(client.getProtocols()));
			client.getChannel().writeAndFlush(outWrapper);
		}

		if (to == null || to.getOnlineClient() == null || to.getOnlineClient().size() == 0) {
			outWrapper = new MessageWrapper(message);
			outWrapper.addAllDescription(wrapper);
			outWrapper.addDescription(MessageWrapper.receiverKey, targetId);
			offlineMessageManager.putOfflineMessage(targetId, outWrapper);
		} else {
			for (Client client : to.getOnlineClient()) {
				outWrapper = new MessageWrapper(message);
				outWrapper.addAllDescription(wrapper);
				outWrapper.addDescription(MessageWrapper.receiverKey, targetId);
				outWrapper.addDescription(MessageWrapper.protocolKey,
						supportedProtocol.getOneSupportedProtocol(client.getProtocols()));
				client.getChannel().writeAndFlush(outWrapper);
			}
		}
	}
}
