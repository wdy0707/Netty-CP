package cn.wdy07.server.handler;

import java.util.List;

import cn.wdy07.server.protocol.ProtocolHandlerNode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ProtocolDecoder extends ByteToMessageDecoder {

	List<ProtocolHandlerNode> protocols;

	public ProtocolDecoder(List<ProtocolHandlerNode> protocols) {
		super();
		this.protocols = protocols;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		boolean needDecode = true;
		
		while (needDecode) {
			needDecode = false;
			
			for (ProtocolHandlerNode node : protocols) {
				if (node.getHandler().containsAtLeastOnePacket(in)) {
					out.addAll(node.getHandler().decode(in));
					
					// 考虑buf中包含两种协议以上的包的情况。当解析完一种包以后，对每种协议重新判断是否有该协议的包，都没有则跳出循环。 
					needDecode = false;
					break;
				}
			}
		}
		

	}
}
