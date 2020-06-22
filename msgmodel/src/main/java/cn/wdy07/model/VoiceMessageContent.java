package cn.wdy07.model;

public class VoiceMessageContent extends MessageContent {
	private String url;
	private int duration;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public VoiceMessageContent(String url, int duration) {
		super();
		this.url = url;
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
}
