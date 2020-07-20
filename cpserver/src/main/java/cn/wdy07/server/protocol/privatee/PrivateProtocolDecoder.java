package cn.wdy07.server.protocol.privatee;

import java.util.List;

import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PrivateProtocolDecoder extends ByteToMessageDecoder {
	private PrivateProtocolCodec codec = new PrivateProtocolCodec();
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (codec.containsAtLeastOnePacket(in))
			out.add(new MessageWrapper(codec.decode(in)));
	}

}
