package cn.wdy07.model;

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
		super();
	}
	
	
	
	
}
