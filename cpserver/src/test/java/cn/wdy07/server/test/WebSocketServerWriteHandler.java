package cn.wdy07.server.test;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketServerWriteHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
		ChannelFuture writeAndFlush = ctx.channel().writeAndFlush(new TextWebSocketFrame((String) msg));
		System.out.println("server--->服务端返回的信息:" + writeAndFlush);
		
	}

}
