package cn.wdy07.server.protocol.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.server.test.WebSocketServerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebsocketPrehandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof WebSocketFrame) {
			WebSocketFrame wsf = (WebSocketFrame) msg;
			// 如果是PING请求就响应PONG
			if (wsf instanceof PingWebSocketFrame) {
				ctx.channel().write(new PongWebSocketFrame(wsf.content().retain()));
				return;
			}
			// 如果是文本消息就转成TextWebSocketFrame
			if (wsf instanceof TextWebSocketFrame) {
				TextWebSocketFrame twsf = (TextWebSocketFrame) wsf;
				// 收到的请求报文
				String requestText = twsf.text();
				System.out.println("server--->收到客户端发的消息:" + requestText);
				ctx.fireChannelRead(wsf.retain());
				// 回写数据
//               ChannelFuture writeAndFlush = ctx.channel().writeAndFlush(new TextWebSocketFrame("你好"));
//               System.out.println("server--->服务端返回的信息:" + writeAndFlush);

			}
		}
		
		logger.info("received websocket frame: {}", msg.getClass().getSimpleName());

	}

}
