package cn.wdy07.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import ch.qos.logback.core.net.server.Client;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.MessageHandlerNode;
import cn.wdy07.server.handler.MessageInboundHandler;
import cn.wdy07.server.handler.ProtocolDecoder;
import cn.wdy07.server.handler.ProtocolEncoder;
import cn.wdy07.server.protocol.ProtocolHandlerNode;
import cn.wdy07.server.protocol.ProtocolHanlder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerInitializer {
	private List<ProtocolHandlerNode> protocols = new ArrayList<ProtocolHandlerNode>();
	private List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
	private int port;

	public ServerInitializer protocol(String name, ProtocolHanlder handler) {
		if (name == null || name.length() == 0 || handler == null)
			throw new IllegalArgumentException("protocol name cant be empty or handler cant be null");

		protocols.add(new ProtocolHandlerNode(name, handler));
		return this;
	}

	public ServerInitializer messageHandler(MessageHandler hanlder) {
		handlers.add(new MessageHandlerNode(0, 0, 0, hanlder));
		return this;
	}

	public ServerInitializer messageHandler(int conversationType, int messageType1, int messageType2, MessageHandler hanlder) {
		if (conversationType < 0 || conversationType > 0x7f)
			throw new IllegalArgumentException("conversationType is byte length");
		if (messageType1 < 0 || messageType1 > 0x7f)
			throw new IllegalArgumentException("messageType1 is byte length");
		if (messageType2 < 0 || messageType2 > 0x7f)
			throw new IllegalArgumentException("messageType2 is byte length");

		handlers.add(new MessageHandlerNode(conversationType, messageType1, messageType2, hanlder));
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
		bootstrap.channel(NioServerSocketChannel.class)
				.group(mainGroup, subGroup)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new ProtocolDecoder(protocols));
						ch.pipeline().addLast(new ProtocolEncoder(protocols));
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
