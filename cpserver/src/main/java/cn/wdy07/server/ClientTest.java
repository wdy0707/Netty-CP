package cn.wdy07.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cn.wdy07.model.Message;
import cn.wdy07.model.MessageHeader;
import cn.wdy07.model.Protocol;
import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.model.content.TextMessageContent;
import cn.wdy07.model.header.ClientType;
import cn.wdy07.model.header.ContentSubType;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.model.header.MessageType;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.MessageHandlerNode;
import cn.wdy07.server.handler.MessageInboundHandler;
import cn.wdy07.server.handler.qualifier.HandlerAllQualifier;
import cn.wdy07.server.protocol.PrivateProtocolHandler;
import cn.wdy07.server.protocol.message.MessageBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

public class ClientTest {

	public static void main(String[] args) {
		new ServerInitializer().func();
	}

	static class ServerInitializer implements Runnable {
		private volatile ChannelFuture future;
		private List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
		private int port;

		public ChannelFuture getFuture() {
			return future;
		}

		public ServerInitializer messageHandler(MessageHandler hanlder) {
			handlers.add(new MessageHandlerNode(HandlerAllQualifier.handlerAllQualifier, hanlder));
			return this;
		}

		public ServerInitializer bind(int port) {
			this.port = port;
			return this;
		}

		public void start() {
			NioEventLoopGroup mainGroup = new NioEventLoopGroup(1);

			Bootstrap b = new Bootstrap();
			b.group(mainGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.AUTO_READ, true);
			b.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new ByteToMessageDecoder() {

						@Override
						protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
								throws Exception {
							out.add(new PrivateProtocolHandler().decode(in));

						}
					});
					ch.pipeline().addLast(new MessageToByteEncoder<Message>() {

						@Override
						protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
							out.writeBytes(new PrivateProtocolHandler().encode(msg));
						}

					});
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							System.out.println(msg);
						}
						
					});
				}

			});

			try {
				ChannelFuture future = b.connect("127.0.0.1", port).sync();
				this.future = future;
				System.out.println("client connected: " + port);
				future.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				try {
					mainGroup.shutdownGracefully().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			this.bind(8080).start();
		}

		public void func() {
			ServerInitializer client = new ServerInitializer();
			new Thread(client).start();
			ChannelFuture future = null;
			Scanner in = new Scanner(System.in);
			while (true) {
				try {
					// 获取future，线程有等待处理时间
					if (null == future) {
						future = client.getFuture();
						Thread.sleep(500);
						continue;
					}

					sendMessage(future.channel(), in.nextLine());

				} catch (Exception e) {
					e.printStackTrace();
					in.close();
				} finally {
					
				}
			}
		}
		
		// group1: user1, user2, user3
		// group2: user1, user2, user4
		// group3: user1, user3, user5
		// login
		// logout
		// heartbeat
		// private
		// group
		public static void sendMessage(Channel ch, String line) {
			Message message = constructMessage(line);
			if (message != null) {
				System.out.println(message);
				ch.writeAndFlush(message);
			}
		}
		
		private static Message constructMessage(String line) {
			String[] split = line.split("\\|");
			
			if (line.startsWith("login")) {
				LoginRequestMessageContent loginMessageContent = new LoginRequestMessageContent();
				loginMessageContent.setSupportedProtocols(new ArrayList<Protocol>(Arrays.asList(Protocol.privatee)));
				loginMessageContent.setClientType(ClientType.values()[Integer.valueOf(split[2])]);
				// login|userId|clientType
				Message message = MessageBuilder.create()
						.convesationType(ConversationType.LOGIN)
						.userId(split[1])
						.content(loginMessageContent)
						.build();
				message.setContent(loginMessageContent);
				return message;
			} else if (line.startsWith("logout")) {
				// logout|userId
				return MessageBuilder.create()
						.convesationType(ConversationType.LOGOUT)
						.userId(split[1])
						.build();
			} else if (line.startsWith("heartbeat")) {
				// heartbeat|userId
				return MessageBuilder.create()
						.convesationType(ConversationType.HEARTBEAT)
						.userId(split[1])
						.build();
			} else if (line.startsWith("private")) {
				TextMessageContent content = new TextMessageContent();
				content.setText(split[3]);
				
				// private|userId|targetId|content
				return MessageBuilder.create()
						.convesationType(ConversationType.PRIVATE)
						.userId(split[1])
						.targetId(split[2])
						.content(content)
						.build();
			} else if (line.startsWith("group")) {
				TextMessageContent content = new TextMessageContent();
				content.setText(split[3]);
				
				// group|userId|targetId|content
				return MessageBuilder.create()
						.convesationType(ConversationType.GROUP)
						.userId(split[1])
						.targetId(split[2])
						.content(content)
						.build();
			}
			
			return null;
		}

	}
}
