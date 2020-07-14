package cn.wdy07.server.protocol;

import java.util.List;

import cn.wdy07.server.CPServerContext;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 服务端解码器，支持多种协议，在服务端启动时通过{@link ServerInitializer#protocols()}注册进来
 * 
 * @author taylor
 */
public class ProtocolDecoder extends ByteToMessageDecoder {
	SupportedProtocol supportedProtocol = CPServerContext.getContext().getConfigurator().getSupportedProtocol();

	public ProtocolDecoder() {
		super();
	}
	
	/**
	 * 遍历所有协议，判断数据是哪一种协议，然后解码成Message
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		boolean needDecode = true;

		while (needDecode) {
			needDecode = false;

			for (ProtocolCodec codec : supportedProtocol.getAllCodec()) {
				if (codec.containsAtLeastOnePacket(in)) {
					out.add(new MessageWrapper(codec.decode(in)));

					// 考虑buf中包含两种协议以上的包的情况。当解析完一种包以后，对每种协议重新判断是否有该协议的包，都没有则跳出循环。
					needDecode = true;
					break;
				}
			}
		}

	}
}
