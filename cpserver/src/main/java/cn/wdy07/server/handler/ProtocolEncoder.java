package cn.wdy07.server.handler;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.server.client.Clients;
import cn.wdy07.server.protocol.ProtocolHandlerNode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<Message> {
	List<ProtocolHandlerNode> protocols;

	public ProtocolEncoder(List<ProtocolHandlerNode> protocols) {
		super();
		this.protocols = protocols;
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		String protocol = Clients.getProtocol(ctx.channel());
		boolean encoded = false;
		
		for (ProtocolHandlerNode node : protocols) {
			if (node.getProtocolName().equals(protocol)) {
				out.writeBytes(node.getHandler().encode(msg));
				encoded = true;
				System.out.println("eocoded");
				break;
			}
		}
		
		if (!encoded)
			throw new IllegalStateException("no proper protocol encoder: " + protocol);
	}

}
