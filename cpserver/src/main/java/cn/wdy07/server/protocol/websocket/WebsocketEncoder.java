package cn.wdy07.server.protocol.websocket;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class WebsocketEncoder extends MessageToMessageEncoder<MessageWrapper> {
	private WebsocketMessageCodec codec = new WebsocketMessageCodec();
	
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageWrapper msg, List<Object> out) throws Exception {
		out.add(codec.encode(msg.getMessage()));
	}

}
