package cn.wdy07.model.content;

import java.util.List;

import cn.wdy07.model.MessageContent;
import cn.wdy07.model.Protocol;

public class LoginMessageContent extends MessageContent {
	private List<Protocol> supportedProtocols;
	
	// 客户端向应用服务器登陆时，应用服务器发放的token
	private String token;
	
	private int code;
	
	private String text;

	public LoginMessageContent() {
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "LoginMessageContent [supportedProtocols=" + supportedProtocols + ", token=" + token + ", code=" + code
				+ ", text=" + text + "]";
	}
	
	
}
