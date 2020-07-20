package cn.wdy07.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.wdy07.model.Message;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.MessageHandlerNode;
import cn.wdy07.server.handler.qualifier.HandlerAllQualifier;
import cn.wdy07.server.protocol.privatee.PrivateProtocolCodec;
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

/**
 * 获取命令行输入利用反射调用SendMessageUtil里面的方法 语法，空格分隔，转义为" 方法名 参数1 参数2 参数3
 * 方法名或参数中间如有空格或"，使用"将方法名或参数包裹起来，使用""转义" 例如: pchat user1 user2 "I""am a boy."
 * 第三个参数为I"am a boy.
 * 
 * @author jinzhiqiang
 *
 */
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
							out.add(new PrivateProtocolCodec().decode(in));

						}
					});
					ch.pipeline().addLast(new MessageToByteEncoder<Message>() {

						@Override
						protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
							out.writeBytes(new PrivateProtocolCodec().encode(msg));
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
			this.bind(18887).start();
		}

		public void func() {
			ServerInitializer client = new ServerInitializer();
			new Thread(client).start();
			ChannelFuture future = null;
			Scanner in = new Scanner(System.in);
			CommandLineUtil util = null;
			while (true) {
				try {
					// 获取future，线程有等待处理时间
					if (null == future) {
						future = client.getFuture();
						if (future != null)
							util = new CommandLineUtil(new SendMessageUtil(future.channel()));
						Thread.sleep(500);
						continue;
					}
					util.invoke(in.nextLine());

				} catch (Exception e) {
					e.printStackTrace();
					in.close();
				} finally {

				}
			}
		}
	}
}
