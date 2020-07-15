package cn.wdy07.server.user;

import java.util.Set;

import io.netty.channel.Channel;

public interface OnlineUserRepository {
	User getUser(String userId);
	Client getClient(String userId, Channel channel);
	Set<String> getUserIds();
	
	void addClient(Client client);
	
	void deleteClient(String userId, Channel channel);
	void deleteUser(String userId);
}
