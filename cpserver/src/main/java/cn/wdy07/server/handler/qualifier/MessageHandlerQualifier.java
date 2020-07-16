package cn.wdy07.server.handler.qualifier;

import cn.wdy07.server.handler.MessageHandlerNode;
import cn.wdy07.server.handler.MessageInboundHandler;
import cn.wdy07.server.protocol.message.MessageWrapper;

/**
 * 判断一条message是否满足处理条件，满足条件返回true
 * 
 * @author taylor
 * @see MessageHandlerNode
 * @see MessageInboundHandler
 */
public interface MessageHandlerQualifier {
	boolean qualify(MessageWrapper wrapper);
}
