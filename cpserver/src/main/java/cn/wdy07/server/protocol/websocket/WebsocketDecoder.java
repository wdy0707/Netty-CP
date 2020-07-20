package cn.wdy07.server.protocol.websocket;

import java.util.List;

import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebsocketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
	private WebsocketMessageCodec codec = new WebsocketMessageCodec();
	
	@Override
	protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
		out.add(new MessageWrapper(codec.decode(msg)));
	}

}
