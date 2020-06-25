package cn.wdy07.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.wdy07.msgmodel.*;
import cn.wdy07.msgmodel.ContentSubType;
import cn.wdy07.model.MessageHeader;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.MessageHandlerNode;
import cn.wdy07.server.handler.MessageInboundHandler;
import cn.wdy07.server.handler.ProtocolDecoder;
import cn.wdy07.server.handler.ProtocolEncoder;
import cn.wdy07.server.handler.business.MessageStoreHandler;
import cn.wdy07.server.handler.function.HeartBeatHandler;
import cn.wdy07.server.protocol.PrivateProtocolHandler;
import cn.wdy07.server.protocol.ProtocolHandlerNode;
import cn.wdy07.server.protocol.ProtocolHanlder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.Future;

public class ClientTest {
	
	
	
	public static void main(String[] args) {
		new ServerInitializer().func();
	}
	
	
	
	static class ServerInitializer implements Runnable {
		private volatile ChannelFuture future;
		private List<ProtocolHandlerNode> protocols = new ArrayList<ProtocolHandlerNode>();
		private List<MessageHandlerNode> handlers = new ArrayList<MessageHandlerNode>();
		private int port;
		
		public ChannelFuture getFuture() {
			return future;
		}

		public ServerInitializer protocol(String name, ProtocolHanlder handler) {
			if (name == null || name.length() == 0 || handler == null)
				throw new IllegalArgumentException("protocol name cant be empty or handler cant be null");

			protocols.add(new ProtocolHandlerNode(name, handler));
			return this;
		}

		public ServerInitializer messageHandler(MessageHandler hanlder) {
			handlers.add(new MessageHandlerNode(-1, -1, -1, hanlder));
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

            Bootstrap b = new Bootstrap();
            b.group(mainGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new ByteToMessageDecoder() {
								
								@Override
								protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
									out.add(new PrivateProtocolHandler().decode(in));
									
								}
							});
							ch.pipeline().addLast(new MessageToByteEncoder<Message>() {

								@Override
								protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out)
										throws Exception {
									out.writeBytes(new PrivateProtocolHandler().encode(msg));
								}
								
							});
							ch.pipeline().addLast(new MessageInboundHandler(handlers));
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
			this
    		.protocol("private", new PrivateProtocolHandler())
    		.messageHandler(0, 0, 0, new MessageHandler() {
				
				@Override
				public void handle(ChannelHandlerContext ctx, Message message) {
					System.out.println(message);
				}
			})
    		.bind(8080)
    		.start();
		}
		
		public void func() {
	        ServerInitializer client = new ServerInitializer();
	        new Thread(client).start();
	        ChannelFuture future = null;
	        Scanner in = new Scanner(System.in);
	        while (true) {
	            try {
	                //获取future，线程有等待处理时间
	                if (null == future) {
	                    future = client.getFuture();
	                    Thread.sleep(500);
	                    continue;
	                }
	                
	                sendMessage(future.channel(), in.nextLine());
	                
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		}
		
		
		// heartbeat
		// chat
		public static void sendMessage(Channel ch, String line) {
			Message message = new Message();
			if (line.equals("heartbeat")) {
				MessageHeader header = new MessageHeader();
				header.setConversationType(ConversationType.HEARTBEAT);
				message.setHeader(header);
			} else {
				MessageHeader header = new MessageHeader();
				header.setConversationType(ConversationType.PRIVATE);
				header.setMessageType1(MessageType.CONTENT);
				header.setMessageType2(ContentSubType.TEXT);
				message.setHeader(header);
				message.setContent(new TextMessageContent("1234"));
			}
			
			ch.writeAndFlush(message);
			System.out.println("message sent");
		}


	}
}
