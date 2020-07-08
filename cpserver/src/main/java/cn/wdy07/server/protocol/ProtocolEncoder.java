package cn.wdy07.server.protocol;

import cn.wdy07.model.Message;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<Message> {
	ClientManager manager = InMemeoryClientManager.getInstance();

	public ProtocolEncoder() {
		super();
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		Protocol protocol = manager.getSupportedProtocol(msg.getHeader().getUserId(), ctx.channel());
		boolean encoded = false;
		
		for (ProtocolHandlerNode node : SupportedProtocol.getInstance().getAllCodec()) {
			if (node.getProtocol() == protocol) {
				out.writeBytes(node.getCodec().encode(msg));
				encoded = true;
				break;
			}
		}
		
		if (!encoded)
			throw new IllegalStateException("no proper protocol encoder: " + protocol);
	}

}
