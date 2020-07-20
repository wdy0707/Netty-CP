package cn.wdy07.model.content;

import cn.wdy07.model.MessageContent;
import cn.wdy07.model.header.ClientType;

public class LoginRequestMessageContent extends MessageContent {

	private ClientType clientType;

	// 客户端向应用服务器登陆时，应用服务器发放的token
	private String token;

	public LoginRequestMessageContent() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	@Override
	public String toString() {
		return "LoginRequestMessageContent [clientType=" + clientType + ", token=" + token + "]";
	}

}
