package cn.wdy07.server.protocol.websocket;

import com.alibaba.fastjson.JSON;

import cn.wdy07.model.Message;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebsocketMessageCodec {
	public Message decode(TextWebSocketFrame frame) {
		String text = frame.text();
		return JSON.parseObject(text, Message.class);
	}
	
	public TextWebSocketFrame encode(Message message) {
		String text =  JSON.toJSONString(message);
		return new TextWebSocketFrame(text);
	}
	
	
}
