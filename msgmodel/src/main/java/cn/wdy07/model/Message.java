package cn.wdy07.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.model.content.LoginResponseMessageContent;
import cn.wdy07.model.content.TextMessageContent;

/**
 * @author wdy
 * @create 2020-06-20 23:54
 */
public final class Message {

    private MessageHeader header;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "typeName")  
    @JsonSubTypes({ 
    		@JsonSubTypes.Type(value = LoginRequestMessageContent.class, name = "cn.wdy07.model.content.LoginRequestMessageContent"),  
            @JsonSubTypes.Type(value = LoginResponseMessageContent.class, name = "cn.wdy07.model.content.LoginResponseMessageContent"),
            @JsonSubTypes.Type(value = TextMessageContent.class, name = "cn.wdy07.model.content.TextMessageContent")})  
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
