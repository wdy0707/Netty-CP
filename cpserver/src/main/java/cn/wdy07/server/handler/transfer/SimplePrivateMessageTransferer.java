package cn.wdy07.server.handler.transfer;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.server.client.Client;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;

public class SimplePrivateMessageTransferer implements MessageTransferer {
	private OfflineMessageManager offlineMessageManager = InMemoryOfflineMessageManager.getInstance();
	private ClientManager clientManager = InMemeoryClientManager.getInstance();
	
	@Override
	public void transfer(Message message) {
		String userId = message.getHeader().getUserId();
		String targetId = message.getHeader().getTargetId();
		
		List<Client> from = clientManager.getUserClients(userId);
		List<Client> to = clientManager.getUserClients(targetId);
		
		for (Client client : from) 
			client.getChannel().writeAndFlush(message);
		
		if (to == null || to.isEmpty())
			offlineMessageManager.putOfflineMessage(targetId, message);
		else
			for (Client client : to)
				client.getChannel().writeAndFlush(message);
	}
}
