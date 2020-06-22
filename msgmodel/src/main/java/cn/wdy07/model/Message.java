package cn.wdy07.model;

/**
 * @author wdy
 * @create 2020-06-20 23:54
 */
public final class Message {

    private MsgHeader header;

    private MessageContent content;

    public Message(MsgHeader header, MessageContent content) {
        this.header = header;
        this.content = content;
    }

    public Message() {
    }

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(MessageContent body) {
        this.content = body;
    }

	@Override
	public String toString() {
		return "Message [header=" + header + ", content=" + content + "]";
	}
}
