package cn.wdy07.server.handler;

import java.util.ArrayList;
import java.util.List;

import cn.wdy07.msgmodel.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageInboundHandler extends SimpleChannelInboundHandler<Message> {

	private List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
	
	
	public MessageInboundHandler(List<MessageHandlerNode> handlers) {
		super();
		this.handlers = handlers;
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		for (MessageHandlerNode node : handlers) {
			if (qualify(node, msg))
				node.hanlder.handle(ctx, msg);
		}
	}
	
	private static boolean qualify(MessageHandlerNode node, Message message) {
		if (node.convesationType != 0 && node.convesationType != message.getHeader().getConversationType())
			return false;
		
		if (node.messageType1 != 0 && node.messageType1 != message.getHeader().getMessageType1())
			return false;
		
		if (node.messageTYpe2 != 0 && node.messageTYpe2 != message.getHeader().getMessageType2())
			return false;
		
		return true;
	}

}
