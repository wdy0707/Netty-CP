package cn.wdy07.server.handler.offline;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.wdy07.model.Message;

public class InMemoryOfflineMessageManager implements OfflineMessageManager {
	private ConcurrentHashMap<String, List<Message>> offlineMessage = new ConcurrentHashMap<String, List<Message>>();
	
	private static InMemoryOfflineMessageManager manager = new InMemoryOfflineMessageManager();
	
	private static final ArrayList<Message> EMPTY_LIST = new ArrayList<Message>();
	private InMemoryOfflineMessageManager() {
		
	}
	
	public static InMemoryOfflineMessageManager getInstance() {
		return manager;
	}
	
	@Override
	public void putOfflineMessage(String userId, Message message) {
		List<Message> list = offlineMessage.putIfAbsent(userId, new ArrayList<Message>());
		if (list == null)
			list = offlineMessage.get(userId);
		
		synchronized (list) {
			list.add(message);
		}
	}

	@Override
	public List<Message> getOfflineMessage(String userId) {
		List<Message> list = offlineMessage.put(userId, new ArrayList<Message>());
		if (list == null || list.isEmpty())
			return EMPTY_LIST;
		return list;
	}

}
