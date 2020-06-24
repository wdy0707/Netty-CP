package cn.wdy07.msgmodel;

import cn.wdy07.model.MessageHeader;
import cn.wdy07.msgmodel.MessageContent;

/**
 * @author wdy
 * @create 2020-06-20 23:54
 */
public final class Message {

    private MessageHeader header;

    private MessageContent content;

    public Message(MessageHeader header, MessageContent content) {
        this.header = header;
        this.content = content;
    }

    public Message() {
    }

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
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
