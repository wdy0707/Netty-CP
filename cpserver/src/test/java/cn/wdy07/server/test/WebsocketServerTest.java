package cn.wdy07.server.test;

import cn.wdy07.server.handler.MessageInboundHandler;
import cn.wdy07.server.protocol.ProtocolDecoder;
import cn.wdy07.server.protocol.ProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class WebsocketServerTest {
	public static void main(String[] args) {
		new WebsocketServerTest().start();
	}
	
	public void start() {
		NioEventLoopGroup mainGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup subGroup = new NioEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.channel(NioServerSocketChannel.class).group(mainGroup, subGroup)
				.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
				        // 将请求与应答消息编码或者解码为HTTP消息
				        pipeline.addLast(new HttpServerCodec());
				        // 将http消息的多个部分组合成一条完整的HTTP消息
				        pipeline.addLast(new HttpObjectAggregator(1024 * 10));
				        // 向客户端发送HTML5文件。主要用于支持浏览器和服务端进行WebSocket通信
				        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
				        // 服务端Handler
				        pipeline.addLast(new WebSocketServerHandler());
					}

				});

		try {
			ChannelFuture future = bootstrap.bind(8081).sync();
			System.out.println("Server started on port: " + 8081);
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				mainGroup.shutdownGracefully().sync();
				subGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
