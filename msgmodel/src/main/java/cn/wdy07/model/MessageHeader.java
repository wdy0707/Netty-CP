package cn.wdy07.model;

import cn.wdy07.model.header.ConversationType;
import cn.wdy07.model.header.MessageSubType;
import cn.wdy07.model.header.MessageType;

/**
 * @author wdy
 * @create 2020-06-20 23:52
 */
public final class MessageHeader {

	// 会话类型
	private ConversationType conversationType;

	// 消息大类
	private MessageType messageType;

	// 消息细分类别
	private MessageSubType messageType2;

	// 通信对象ID
	private String targetId;

	// 自身ID
	private String userId;

	public ConversationType getConversationType() {
		return conversationType;
	}

	public void setConversationType(ConversationType conversationType) {
		this.conversationType = conversationType;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public MessageSubType getMessageType2() {
		return messageType2;
	}

	public void setMessageType2(MessageSubType messageType2) {
		this.messageType2 = messageType2;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "MessageHeader [conversationType=" + conversationType + ", messageType=" + messageType
				+ ", messageType2=" + messageType2 + ", targetId=" + targetId + ", userId=" + userId + "]";
	}

}
