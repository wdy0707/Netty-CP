package cn.wdy07.server.handler.offline;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.server.protocol.message.MessageWrapper;

/**
 * 离线消息存取接口，该接口实现也必须是单例的
 * @author taylor
 *
 */
public interface OfflineMessageManager {
	void putOfflineMessage(String userId, MessageWrapper wrapper);
	
	List<MessageWrapper> getOfflineMessage(String userId);
}
