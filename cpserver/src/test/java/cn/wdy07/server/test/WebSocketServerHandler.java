package cn.wdy07.server.test;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	
	private static final Logger log = LoggerFactory.getLogger(WebSocketServerHandler.class);
	
	@Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
             log.info("HTTP请求");
        } else if (msg instanceof WebSocketFrame) {
            WebSocketFrame wsf = (WebSocketFrame) msg;
            // 如果是PING请求就响应PONG
            if (wsf instanceof PingWebSocketFrame) {
                 log.debug("响应PONG");
                ctx.channel().write(new PongWebSocketFrame(wsf.content().retain()));
                return;
            }
            // 如果是文本消息就转成TextWebSocketFrame
            if (wsf instanceof TextWebSocketFrame) {
                TextWebSocketFrame twsf = (TextWebSocketFrame) wsf;
                // 收到的请求报文
                String requestText = twsf.text();
                System.out.println("server--->收到客户端发的消息:" + requestText);
                // 回写数据
                ChannelFuture writeAndFlush = ctx.channel().writeAndFlush(new TextWebSocketFrame("你好"));
                System.out.println("server--->服务端返回的信息:" + writeAndFlush);
  
            }
        }
    }

 
}
