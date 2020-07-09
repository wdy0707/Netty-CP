package cn.wdy07.server.handler.transfer;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.client.Client;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.protocol.message.MessageWrapper;

public class SimplePrivateMessageTransferer implements MessageTransferer {
	private OfflineMessageManager offlineMessageManager = CPServerContext.getContext().getConfigurator().getOfflineMessageManager();
	private ClientManager clientManager = CPServerContext.getContext().getConfigurator().getClientManager();
	
	@Override
	public void transfer(MessageWrapper wrapper) {
		Message message = wrapper.getMessage();
		String userId = message.getHeader().getUserId();
		String targetId = message.getHeader().getTargetId();
		
		List<Client> from = clientManager.getUserClients(userId);
		List<Client> to = clientManager.getUserClients(targetId);
		
		MessageWrapper outWrapper;
		for (Client client : from)  {
			outWrapper = new MessageWrapper(message);
			outWrapper.addAllDescription(wrapper);
			outWrapper.addDescription(MessageWrapper.receiverKey, userId);
			outWrapper.addDescription(MessageWrapper.protocolKey, client.getOneSupportProtocol());
			client.getChannel().writeAndFlush(outWrapper);
		}
		
		if (to == null || to.isEmpty()) {
			outWrapper = new MessageWrapper(message);
			outWrapper.addAllDescription(wrapper);
			outWrapper.addDescription(MessageWrapper.receiverKey, targetId);
			offlineMessageManager.putOfflineMessage(targetId, outWrapper);
		} else {
			for (Client client : to) {
				outWrapper = new MessageWrapper(message);
				outWrapper.addAllDescription(wrapper);
				outWrapper.addDescription(MessageWrapper.receiverKey, targetId);
				outWrapper.addDescription(MessageWrapper.protocolKey, client.getOneSupportProtocol());
				client.getChannel().writeAndFlush(outWrapper);
			}
		}
	}
}
