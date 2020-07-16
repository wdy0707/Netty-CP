package cn.wdy07.server.handler;

import java.util.ArrayList;
import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 对Message的处理，服务端启动时通过ServerInitializer#messageHandler注册进来
 * 
 * @author taylor
 *
 */
public class MessageInboundHandler extends SimpleChannelInboundHandler<MessageWrapper> {

	private List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
	
	public MessageInboundHandler(List<MessageHandlerNode> handlers) {
		super();
		this.handlers = handlers;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageWrapper msg) throws Exception {
		for (MessageHandlerNode node : handlers) {
			if (node.getQualifier().qualify(msg))
				node.getHanlder().handle(ctx, msg);
		}
	}
}
