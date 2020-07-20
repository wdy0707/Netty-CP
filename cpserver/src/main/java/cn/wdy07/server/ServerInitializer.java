package cn.wdy07.server;

import java.util.ArrayList;
import java.util.List;

import cn.wdy07.server.config.CPServerConfigurator;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.MessageHandlerNode;
import cn.wdy07.server.handler.MessageInboundHandler;
import cn.wdy07.server.handler.qualifier.HandlerAllQualifier;
import cn.wdy07.server.handler.qualifier.MessageHandlerQualifier;
import cn.wdy07.server.protocol.privatee.PrivateProtocolDecoder;
import cn.wdy07.server.protocol.privatee.PrivateProtocolEncoder;
import cn.wdy07.server.protocol.websocket.WebsocketDecoder;
import cn.wdy07.server.protocol.websocket.WebsocketEncoder;
import cn.wdy07.server.protocol.websocket.WebsocketPrehandler;
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

public class ServerInitializer {
	private List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
	private int websocketPort = -1;
	private int privatePort = -1;

	public ServerInitializer() {

	}

	public ServerInitializer(CPServerConfigurator configurator) {
		CPServerContext.getContext().setConfigurator(configurator);
	}

	public ServerInitializer messageHandler(MessageHandlerQualifier qualifier, MessageHandler handler) {
		handlers.add(new MessageHandlerNode(qualifier, handler));
		return this;
	}

	public ServerInitializer messageHandler(MessageHandler handler) {
		handlers.add(new MessageHandlerNode(HandlerAllQualifier.handlerAllQualifier, handler));
		return this;
	}

	public ServerInitializer bindWebsocket(int websocketPort) {
		this.websocketPort = websocketPort;
		return this;
	}

	public ServerInitializer bindPrivate(int privatePort) {
		this.privatePort = privatePort;
		return this;
	}

	public void start() {
		NioEventLoopGroup subGroup = new NioEventLoopGroup();
		MessageInboundHandler handler = new MessageInboundHandler(handlers);

		if (privatePort >= 1024 && privatePort <= 65535) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					NioEventLoopGroup mainGroup = new NioEventLoopGroup(1);

					ServerBootstrap bootstrap = new ServerBootstrap();
					bootstrap.channel(NioServerSocketChannel.class).group(mainGroup, subGroup)
							.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<Channel>() {

								@Override
								protected void initChannel(Channel ch) throws Exception {
									ch.pipeline().addLast(new PrivateProtocolDecoder());
									ch.pipeline().addLast(new PrivateProtocolEncoder());
									ch.pipeline().addLast(handler);
								}

							});

					try {
						ChannelFuture future = bootstrap.bind(privatePort).sync();
						System.out.println("Private Server started on port: " + privatePort);
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
			}).start();
		}

		if (websocketPort >= 1024 && websocketPort <= 65535) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					NioEventLoopGroup mainGroup = new NioEventLoopGroup(1);

					ServerBootstrap bootstrap = new ServerBootstrap();
					bootstrap.channel(NioServerSocketChannel.class).group(mainGroup, subGroup)
							.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<Channel>() {

								@Override
								protected void initChannel(Channel ch) throws Exception {
									ChannelPipeline pipeline = ch.pipeline();
									pipeline.addLast(new HttpServerCodec());
									pipeline.addLast(new HttpObjectAggregator(1024 * 10));
									pipeline.addLast(new WebSocketServerProtocolHandler("/"));
									pipeline.addLast(new WebsocketPrehandler());
									pipeline.addLast(new WebsocketDecoder());
									pipeline.addLast(new WebsocketEncoder());
									ch.pipeline().addLast(handler);
								}

							});

					try {
						ChannelFuture future = bootstrap.bind(websocketPort).sync();
						System.out.println("Websocket Server started on port: " + websocketPort);
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
			}).start();
		}

	}

}
