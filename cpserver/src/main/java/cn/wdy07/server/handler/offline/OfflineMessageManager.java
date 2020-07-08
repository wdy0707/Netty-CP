package cn.wdy07.server.handler.offline;

import java.util.List;

import cn.wdy07.model.Message;

/**
 * 离线消息存取接口，该接口实现也必须是单例的
 * @author taylor
 *
 */
public interface OfflineMessageManager {
	void putOfflineMessage(String userId, Message message);
	
	List<Message> getOfflineMessage(String userId);
}
