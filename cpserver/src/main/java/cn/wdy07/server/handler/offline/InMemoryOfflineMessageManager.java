package cn.wdy07.server.handler.offline;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.wdy07.model.Message;
import cn.wdy07.server.protocol.message.MessageWrapper;

public class InMemoryOfflineMessageManager implements OfflineMessageManager {
	private ConcurrentHashMap<String, List<MessageWrapper>> offlineMessage = new ConcurrentHashMap<String, List<MessageWrapper>>();

	private static final ArrayList<MessageWrapper> EMPTY_LIST = new ArrayList<MessageWrapper>();

	public InMemoryOfflineMessageManager() {

	}

	@Override
	public void putOfflineMessage(String userId, MessageWrapper wrapper) {
		List<MessageWrapper> list = offlineMessage.putIfAbsent(userId, new ArrayList<MessageWrapper>());
		if (list == null)
			list = offlineMessage.get(userId);

		synchronized (list) {
			list.add(wrapper);
		}
	}

	@Override
	public List<MessageWrapper> getOfflineMessage(String userId) {
		List<MessageWrapper> list = offlineMessage.put(userId, new ArrayList<MessageWrapper>());
		if (list == null || list.isEmpty())
			return EMPTY_LIST;
		return list;
	}

}
