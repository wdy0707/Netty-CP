package cn.wdy07.server.handler.transfer;

import java.util.List;
import java.util.Set;

import cn.wdy07.model.Message;
import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.handler.group.GroupManager;
import cn.wdy07.server.handler.group.Member;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.protocol.message.MessageWrapper;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.User;
import cn.wdy07.server.user.UserManager;

public class SimpleGroupMessageTransferer implements MessageTransferer {
	private OfflineMessageManager offlineMessageManager = CPServerContext.getContext().getConfigurator()
			.getGroupOfflineMessageManager();
	private GroupManager groupManager = CPServerContext.getContext().getConfigurator().getGroupManager();
	private UserManager userManager = CPServerContext.getContext().getConfigurator().getUserManager();

	@Override
	public void transfer(MessageWrapper wrapper) {
		Message message = wrapper.getMessage();
		String groupId = message.getHeader().getTargetId();
		Set<Member> members = groupManager.getAllMember(groupId);

		// 将消息发送给每一个成员的每一个设备
		for (Member member : members) {
			String userId = member.getUserId();
			User user = userManager.getUser(userId);
			if (user == null || user.getOnlineClient() == null || user.getOnlineClient().size() == 0) {
				MessageWrapper outWrapper = new MessageWrapper(wrapper);
				offlineMessageManager.putOfflineMessage(userId, outWrapper);
			} else {
				List<Client> clients = user.getOnlineClient();
				for (Client client : clients) {
					MessageWrapper outWrapper = new MessageWrapper(wrapper);
					client.getChannel().writeAndFlush(outWrapper);
				}
			}
		}
	}

}
