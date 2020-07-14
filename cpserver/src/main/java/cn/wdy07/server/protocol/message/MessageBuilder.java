package cn.wdy07.server.protocol.message;

import cn.wdy07.model.Message;
import cn.wdy07.model.MessageContent;
import cn.wdy07.model.MessageHeader;
import cn.wdy07.model.header.ClientType;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.model.header.MessageSubType;
import cn.wdy07.model.header.MessageType;

public class MessageBuilder {
	private Message message;
	
	public static MessageBuilder create() {
		return new MessageBuilder();
	}
	
	private MessageBuilder() {
		message = new Message();
		message.setHeader(new MessageHeader());
	}
	
	public MessageBuilder convesationType(ConversationType type) {
		message.getHeader().setConversationType(type);
		return this;
	}
	
	public MessageBuilder messageType(MessageType messageType) {
		message.getHeader().setMessageType(messageType);
		return this;
	}
	
	public MessageBuilder messageType2(MessageSubType subype) {
		message.getHeader().setMessageType2(subype);
		return this;
	}
	
	public MessageBuilder userId(String userId) {
		message.getHeader().setUserId(userId);
		return this;
	}
	
	public MessageBuilder targetId(String targetId) {
		message.getHeader().setTargetId(targetId);
		return this;
	}
	
	public MessageBuilder content(MessageContent content) {
		message.setContent(content);
		return this;
	}
	
	public Message build() {
		return message;
	}
}
