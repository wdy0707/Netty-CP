package cn.wdy07.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.model.Message;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * 对Message的处理，服务端启动时通过ServerInitializer#messageHandler注册进来
 * 
 * @author taylor
 *
 */

@Sharable
public class MessageInboundHandler extends SimpleChannelInboundHandler<MessageWrapper> {
	private static final Logger logger = LoggerFactory.getLogger(MessageInboundHandler.class); 
	private List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
	
	public MessageInboundHandler(List<MessageHandlerNode> handlers) {
		super();
		this.handlers = handlers;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageWrapper msg) throws Exception {
		for (MessageHandlerNode node : handlers) {
			if (node.getQualifier().qualify(msg)) {
				node.getHanlder().handle(ctx, msg);
				logger.info("{} invoked.", node.getHanlder());
			}
		}
	}
}
