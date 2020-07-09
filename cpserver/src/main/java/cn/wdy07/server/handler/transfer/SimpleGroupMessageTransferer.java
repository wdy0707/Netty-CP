package cn.wdy07.server.handler.transfer;

import java.util.List;
import java.util.Set;

import cn.wdy07.model.Message;
import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.client.Client;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.handler.group.GroupManager;
import cn.wdy07.server.handler.group.Member;
import cn.wdy07.server.handler.group.SimpleLocalGroupManager;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.protocol.message.MessageWrapper;

public class SimpleGroupMessageTransferer implements MessageTransferer {
	private OfflineMessageManager offlineMessageManager = CPServerContext.getContext().getConfigurator().getOfflineMessageManager();
	private ClientManager clientManager = CPServerContext.getContext().getConfigurator().getClientManager();
	private GroupManager groupManager = CPServerContext.getContext().getConfigurator().getGroupManager();
	
	@Override
	public void transfer(MessageWrapper wrapper) {
		Message message = wrapper.getMessage();
		String groupId = message.getHeader().getTargetId();
		Set<Member> members = groupManager.getAllMember(groupId);
		
		// 将消息发送给每一个成员
		for (Member member : members) {
			String userId = member.getUserId();
			wrapper.addDescription(MessageWrapper.receiverKey, userId);
			List<Client> clients = clientManager.getUserClients(userId);
			if (clients == null || clients.isEmpty()) {
				offlineMessageManager.putOfflineMessage(userId, wrapper);
			} else {
				for (Client client : clients) {
					wrapper.addDescription(MessageWrapper.protocolKey, client.getOneSupportProtocol());
					client.getChannel().writeAndFlush(wrapper);
				}
			}
		}
	}

}
