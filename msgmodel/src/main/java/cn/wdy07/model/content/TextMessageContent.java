package cn.wdy07.model.content;

import cn.wdy07.model.MessageContent;

public class TextMessageContent extends MessageContent {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextMessageContent(String text) {
		super();
		this.text = text;
	}

	public TextMessageContent() {
		
	}

	@Override
	public String toString() {
		return "TextMessageContent [text=" + text + "]";
	}
	
	
	
	
}
