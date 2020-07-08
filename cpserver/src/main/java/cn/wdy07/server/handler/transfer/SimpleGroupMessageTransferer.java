package cn.wdy07.server.handler.transfer;

import java.util.List;
import java.util.Set;

import cn.wdy07.model.Message;
import cn.wdy07.server.client.Client;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.handler.group.GroupManager;
import cn.wdy07.server.handler.group.Member;
import cn.wdy07.server.handler.group.SimpleLocalGroupManager;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import io.netty.channel.Channel;

public class SimpleGroupMessageTransferer implements MessageTransferer {
	private OfflineMessageManager offlineMessageManager = InMemoryOfflineMessageManager.getInstance();
	private ClientManager clientManager = InMemeoryClientManager.getInstance();
	private GroupManager groupManager = SimpleLocalGroupManager.getInstance();
	
	@Override
	public void transfer(Message message) {
		String groupId = message.getHeader().getTargetId();
		Set<Member> members = groupManager.getAllMember(groupId);
		for (Member member : members) {
			String userId = member.getUserId();
			List<Client> clients = clientManager.getUserClients(userId);
			if (clients == null || clients.isEmpty()) {
				offlineMessageManager.putOfflineMessage(userId, message);
			} else {
				for (Client client : clients) {
					client.getChannel().writeAndFlush(message);
				}
			}
			

		}
	}

}
