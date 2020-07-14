package cn.wdy07.server.user.token;

import cn.wdy07.model.header.ClientType;

public class SimpleToken implements Token {
	private String token;
	private String userId;
	private ClientType type;
	
	SimpleToken() {
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ClientType getType() {
		return type;
	}

	public void setType(ClientType type) {
		this.type = type;
	}
}
