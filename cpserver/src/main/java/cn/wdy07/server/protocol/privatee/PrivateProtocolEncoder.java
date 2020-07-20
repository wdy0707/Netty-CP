package cn.wdy07.server.protocol.privatee;

import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PrivateProtocolEncoder extends MessageToByteEncoder<MessageWrapper> {
	private PrivateProtocolCodec codec = new PrivateProtocolCodec();
	
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageWrapper wrapper, ByteBuf out) throws Exception {
		out.writeBytes(codec.encode(wrapper.getMessage()));
	}

}
