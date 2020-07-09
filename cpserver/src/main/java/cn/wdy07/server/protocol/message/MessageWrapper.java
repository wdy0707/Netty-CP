package cn.wdy07.server.protocol.message;

import java.util.HashMap;

import cn.wdy07.model.Message;

/**
 * 底层协议被decode成message之后，加入一些描述信息
 * 
 * @author taylor
 *
 */
public class MessageWrapper {
	public static final String receiverKey = "receiver";
	public static final String protocolKey = "protocol";

	private Message message;

	private HashMap<String, Object> description;

	public MessageWrapper(Message message) {
		super();
		this.message = message;
		this.description = new HashMap<String, Object>();
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Object addDescription(String key, Object value) {
		return description.put(key, value);
	}

	public Object getDescription(String key) {
		return description.get(key);
	}

	public void removeDescription(String key) {
		description.remove(key);
	}

	public void clearDescription() {
		description.clear();
	}
	
	public void addAllDescription(MessageWrapper wrapper) {
		this.description.putAll(wrapper.description);
	}
}
