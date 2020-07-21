package cn.wdy07.server.protocol.websocket;


import cn.wdy07.model.Message;
import cn.wdy07.server.JSONUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebsocketMessageCodec {
	public Message decode(TextWebSocketFrame frame) {
		String text = frame.text();
		return JSONUtil.parse(text, Message.class);
	}
	
	public TextWebSocketFrame encode(Message message) {
		String text =  JSONUtil.toJSONString(message);
		return new TextWebSocketFrame(text);
	}
	
	
}
