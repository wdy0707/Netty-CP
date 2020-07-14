package cn.wdy07.server.user;

import java.util.Set;

import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.Channel;

/**
 * @author jinzhiqiang
 *
 */
public interface UserManager {
	void login(MessageWrapper wrapper, Channel channel);
	void logout(MessageWrapper wrapper, Channel channel);
	
	User getUser(String userId);
	Client getClient(String userId, Channel channel);
	Set<String> getUserIds();
	void addClient(Client client);
	void deleteClient(String userId, Channel channel);
}
