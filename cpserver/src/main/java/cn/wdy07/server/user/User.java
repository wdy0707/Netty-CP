package cn.wdy07.server.user;

import java.util.List;

public class User {
	private String userId;
	private List<Client> onlineClient;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Client> getOnlineClient() {
		return onlineClient;
	}

	public void setOnlineClient(List<Client> onlineClient) {
		this.onlineClient = onlineClient;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", onlineClient=" + onlineClient + "]";
	}
}
