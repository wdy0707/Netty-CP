package cn.wdy07.model.content;

import cn.wdy07.model.MessageContent;

public class ImageMessageContent extends MessageContent {
	private String url;
	
	// 是否发送原图
	private boolean isFull;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public ImageMessageContent() {
		super();
	}

	public ImageMessageContent(String url, boolean isFull) {
		super();
		this.url = url;
		this.isFull = isFull;
	}
	
	
}
