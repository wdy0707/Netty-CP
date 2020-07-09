package cn.wdy07.model.content;

import cn.wdy07.model.MessageContent;

public class LoginResponseMessageContent extends MessageContent {
	private int code;
	private String content;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "LoginResponseMessageContent [code=" + code + ", content=" + content + "]";
	}

}
