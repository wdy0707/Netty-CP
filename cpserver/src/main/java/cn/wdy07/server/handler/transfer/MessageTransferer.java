package cn.wdy07.server.handler.transfer;

import cn.wdy07.server.protocol.message.MessageWrapper;

public interface MessageTransferer {
	void transfer(MessageWrapper wrapper);
}
