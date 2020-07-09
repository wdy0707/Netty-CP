package cn.wdy07.model.content;

import java.util.List;

import cn.wdy07.model.MessageContent;
import cn.wdy07.model.Protocol;
import cn.wdy07.model.header.ClientType;

public class LoginRequestMessageContent extends MessageContent {

	private ClientType clientType;
	private List<Protocol> supportedProtocols;

	// 客户端向应用服务器登陆时，应用服务器发放的token
	private String token;

	public LoginRequestMessageContent() {
		super();
	}

	public List<Protocol> getSupportedProtocols() {
		return supportedProtocols;
	}

	public void setSupportedProtocols(List<Protocol> supportedProtocols) {
		this.supportedProtocols = supportedProtocols;
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
		return "LoginRequestMessageContent [clientType=" + clientType + ", supportedProtocols=" + supportedProtocols
				+ ", token=" + token + "]";
	}

}
