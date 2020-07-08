package cn.wdy07.model.content;

import java.util.List;

import cn.wdy07.model.MessageContent;

public class LoginMessageContent extends MessageContent {
	private List<Integer> supportedProtocols;
	
	// 客户端向应用服务器登陆时，应用服务器发放的token
	private String token;

	public List<Integer> getSupportedProtocols() {
		return supportedProtocols;
	}

	public void setSupportedProtocols(List<Integer> supportedProtocols) {
		this.supportedProtocols = supportedProtocols;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
