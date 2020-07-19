package cn.wdy07.server.user.login;

import java.util.List;

import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.User;

public class MultiClientLoginStrategy implements LoginStrategy {

	private static final int defaultMaxLoginClientCount = 3;
	private int maxLoginClientCount = defaultMaxLoginClientCount;

	public MultiClientLoginStrategy() {
		
	}
	
	public MultiClientLoginStrategy(int maxLoginClientCount) {
		this.maxLoginClientCount = maxLoginClientCount;
	}

	@Override
	public boolean canLogin(User user, Client client) {
		if (user == null || user.getOnlineClient() == null || user.getOnlineClient().size() == 0)
			return true;
		else {
			List<Client> onlineClient = user.getOnlineClient();
			if (onlineClient.size() >= maxLoginClientCount)
				return false;
			for (Client c : onlineClient) {
				if (c.getChannel().equals(client.getChannel()))
					return false;

				if (c.getClientType().equals(client.getClientType()))
					return false;
			}
		}
		return true;
	}

}
