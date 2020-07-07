package cn.wdy07.server.handler;

import cn.wdy07.model.Message;

/**
 * 判断一条message是否满足处理条件，满足条件返回true
 * 
 * @author taylor
 * @see MessageHandlerNode
 * @see MessageInboundHandler
 */
public interface MessageHandlerQualifier {
	boolean qualify(Message message);
}
