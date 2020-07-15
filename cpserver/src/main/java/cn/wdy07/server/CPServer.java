package cn.wdy07.server;

import cn.wdy07.model.BaseType;
import cn.wdy07.model.Protocol;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.server.config.DefaultCPServerConfigurator;
import cn.wdy07.server.handler.GroupMessageHandler;
import cn.wdy07.server.handler.HeartBeatHandler;
import cn.wdy07.server.handler.LoginLogoutHandler;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.MessageStoreHandler;
import cn.wdy07.server.handler.PrivateMessageHandler;
import cn.wdy07.server.handler.qualifier.ConversationTypeQualifier;
import cn.wdy07.server.protocol.PrivateProtocolHandler;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wdy
 * @create 2020-06-21 0:56
 */
public class CPServer {
	public static void main(String[] args) {
		new ServerInitializer(new MyConfigurator())
				
				// 支持的协议
				.protocol(Protocol.privatee, new PrivateProtocolHandler())

				// debug 使用，打印每一个收到的报文
				.messageHandler(new MessageHandler() {

					@Override
					public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
						System.out.println(wrapper.getMessage());
					}
				})

				// 对Message的处理
				// 心跳
				.messageHandler(new ConversationTypeQualifier(ConversationType.HEARTBEAT), new HeartBeatHandler())

				// LOGIN、LOGOUT：修改在线客户端、客户端登陆日志
				.messageHandler(
						new ConversationTypeQualifier(
								new BaseType[] { ConversationType.LOGIN, ConversationType.LOGOUT }),
						new LoginLogoutHandler())

				// 私聊、群聊消息历史记录
				.messageHandler(
						new ConversationTypeQualifier(
								new BaseType[] { ConversationType.PRIVATE, ConversationType.GROUP }),
						new MessageStoreHandler())

				// 私聊群聊分开实现
				// 真正的消息转发处理：多客户端同时在线支持、屏蔽词、已读功能都在这里实现
				.messageHandler(new ConversationTypeQualifier(ConversationType.PRIVATE), new PrivateMessageHandler())
				.messageHandler(new ConversationTypeQualifier(ConversationType.GROUP), new GroupMessageHandler())

				.bind(8080).start();
	}

}
