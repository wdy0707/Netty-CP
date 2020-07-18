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

	public MessageWrapper() {
		
	}
	
	public MessageWrapper(Message message) {
		super();
		this.message = message;
		this.description = new HashMap<String, Object>();
	}
	
	public MessageWrapper(MessageWrapper wrapper) {
		super();
		this.message = wrapper.message;
		this.description = new HashMap<String, Object>(wrapper.description);
	}

	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}

	
	
	public HashMap<String, Object> getDescription() {
		return description;
	}

	public void setDescription(HashMap<String, Object> description) {
		this.description = description;
	}

	public Object addDescription(String key, Object value) {
		if (description == null)
			description = new HashMap<String, Object>();
		return description.put(key, value);
	}

	public Object getDescriptionByKey(String key) {
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

	@Override
	public String toString() {
		return "MessageWrapper [message=" + message + ", description=" + description + "]";
	}
	
	
}
