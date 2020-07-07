package cn.wdy07.server.protocol;

import cn.wdy07.model.Message;
import cn.wdy07.server.client.Clients;
import cn.wdy07.server.client.ConcurrentHashMapClients;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<Message> {
	Clients clients = ConcurrentHashMapClients.getInstance();

	public ProtocolEncoder() {
		super();
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		Protocol protocol = clients.getSupportProtocol(ctx.channel());
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
