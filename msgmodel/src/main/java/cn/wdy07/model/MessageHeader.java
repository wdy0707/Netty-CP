package cn.wdy07.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wdy
 * @create 2020-06-20 23:52
 */
public final class MessageHeader {

	// 会话类型
	private byte conversationType;

	// 消息大类
	private byte messageType1;

	// 消息细分类别
	private byte messageType2;

	// 客户端类型
	private byte clientType;

	// 通信对象ID
	private String targetId;

	// 自身ID
	private String userId;

	public short getConversationType() {
		return conversationType;
	}

	public void setConversationType(int conversationType) {
		this.conversationType = (byte) conversationType;
	}

	public byte getMessageType1() {
		return messageType1;
	}

	public void setMessageType1(int messageType1) {
		this.messageType1 = (byte) messageType1;
	}

	public byte getMessageType2() {
		return messageType2;
	}

	public void setMessageType2(int messageType2) {
		this.messageType2 = (byte) messageType2;
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

	public byte getClientType() {
		return clientType;
	}

	public void setClientType(byte clientType) {
		this.clientType = clientType;
	}

	@Override
	public String toString() {
		return "MessageHeader [conversationType=" + conversationType + ", messageType1=" + messageType1
				+ ", messageType2=" + messageType2 + ", clientType=" + clientType + ", targetId=" + targetId
				+ ", userId=" + userId + "]";
	}

}
