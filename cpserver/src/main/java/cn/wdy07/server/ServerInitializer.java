package cn.wdy07.server;

import java.util.ArrayList;
import java.util.List;

import cn.wdy07.model.Protocol;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.MessageHandlerNode;
import cn.wdy07.server.handler.MessageInboundHandler;
import cn.wdy07.server.handler.qualifier.HandlerAllQualifier;
import cn.wdy07.server.handler.qualifier.MessageHandlerQualifier;
import cn.wdy07.server.protocol.ProtocolCodec;
import cn.wdy07.server.protocol.ProtocolDecoder;
import cn.wdy07.server.protocol.ProtocolEncoder;
import cn.wdy07.server.protocol.SupportedProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerInitializer {
	List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
	private int port;

	public ServerInitializer protocol(Protocol protocol, ProtocolCodec codec) {
		SupportedProtocol.getInstance().register(protocol, codec);
		return this;
	}

	public ServerInitializer messageHandler(MessageHandlerQualifier qualifier, MessageHandler handler) {
		handlers.add(new MessageHandlerNode(qualifier, handler));
		return this;
	}
	
	public ServerInitializer messageHandler(MessageHandler handler) {
		handlers.add(new MessageHandlerNode(HandlerAllQualifier.handlerAllQualifier, handler));
		return this;
	}

	public ServerInitializer bind(int port) {
		this.port = port;
		return this;
	}

	public void start() {
		NioEventLoopGroup mainGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup subGroup = new NioEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.channel(NioServerSocketChannel.class).group(mainGroup, subGroup)
				.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new ProtocolDecoder());
						ch.pipeline().addLast(new ProtocolEncoder());
						ch.pipeline().addLast(new MessageInboundHandler(handlers));
					}

				});

		try {
			ChannelFuture future = bootstrap.bind(port).sync();
			System.out.println("Server started on port: " + port);
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
