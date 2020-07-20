package cn.wdy07.server;

import cn.wdy07.model.Message;
import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.model.content.TextMessageContent;
import cn.wdy07.model.header.ClientType;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.server.protocol.message.MessageBuilder;
import io.netty.channel.Channel;

public class SendMessageUtil {
	private Channel channel;
	
	public SendMessageUtil(Channel channel) {
		this.channel = channel;
	}
	
	public void login(String userId, ClientType type) {
		LoginRequestMessageContent loginMessageContent = new LoginRequestMessageContent();
		loginMessageContent.setClientType(type);
		Message message = MessageBuilder.create()
				.convesationType(ConversationType.LOGIN)
				.userId(userId)
				.content(loginMessageContent)
				.build();
		message.setContent(loginMessageContent);
		channel.writeAndFlush(message);
	}
	
	public void logout(String userId) {
		Message message = MessageBuilder.create()
				.convesationType(ConversationType.LOGOUT)
				.userId(userId)
				.build();
		channel.writeAndFlush(message);
	}
	
	public void heartbeat(String userId) {
		Message message = MessageBuilder.create()
				.convesationType(ConversationType.HEARTBEAT)
				.userId(userId)
				.build();
		channel.writeAndFlush(message);
	}
	
	public void pchat(String userId, String targetId, String text) {
		TextMessageContent content = new TextMessageContent();
		content.setText(text);
		Message message = MessageBuilder.create()
				.convesationType(ConversationType.PRIVATE)
				.userId(userId)
				.targetId(targetId)
				.content(content)
				.build();
		channel.writeAndFlush(message);
	}
	
	public void gchat(String userId, String targetId, String text) {
		TextMessageContent content = new TextMessageContent();
		content.setText(text);
		Message message = MessageBuilder.create()
				.convesationType(ConversationType.GROUP)
				.userId(userId)
				.targetId(targetId)
				.content(content)
				.build();
		channel.writeAndFlush(message);
	}
}
