package cn.wdy07.server.protocol;

import cn.wdy07.model.Protocol;
import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<MessageWrapper> {
	Protocol defaultProtocol = Protocol.privatee;
	ClientManager manager = CPServerContext.getContext().getConfigurator().getClientManager();

	public ProtocolEncoder() {
		super();
	}
	
	/*	
	 *  Message中的userId和targetId并不代表这条消息应该发送给谁
	 * @see io.netty.handler.codec.MessageToByteEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.buffer.ByteBuf)
	 */
	
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageWrapper wrapper, ByteBuf out) throws Exception {
		Protocol protocol = null;
		protocol = (Protocol) wrapper.getDescription(MessageWrapper.protocolKey);
		
		if (protocol == null) { // wrapper未指定协议
			String receiverId = (String) wrapper.getDescription(MessageWrapper.receiverKey);
			if (receiverId != null) 
				protocol = manager.getSupportedProtocol(receiverId, ctx.channel());
		}
		
		if (protocol == null) // 仍然没有查找到协议，使用默认的
			protocol = defaultProtocol;
		
		out.writeBytes(SupportedProtocol.getInstance().getCodec(protocol).encode(wrapper.getMessage()));
	}

}
