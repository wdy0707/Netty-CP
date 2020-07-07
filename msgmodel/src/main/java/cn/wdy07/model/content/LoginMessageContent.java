package cn.wdy07.model.content;

import java.util.List;

import cn.wdy07.model.MessageContent;

public class LoginMessageContent extends MessageContent {
	private List<Integer> supportedProtocols;

	public List<Integer> getSupportedProtocols() {
		return supportedProtocols;
	}

	public void setSupportedProtocols(List<Integer> supportedProtocols) {
		this.supportedProtocols = supportedProtocols;
	}
}
