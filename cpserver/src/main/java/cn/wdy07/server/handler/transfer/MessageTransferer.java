package cn.wdy07.server.handler.transfer;

import cn.wdy07.model.Message;

public interface MessageTransferer {
	void transfer(Message message);
}
