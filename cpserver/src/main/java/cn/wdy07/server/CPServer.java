package cn.wdy07.server;

import cn.wdy07.model.header.ConversationType;
import cn.wdy07.server.handler.HeartBeatHandler;
import cn.wdy07.server.handler.LoginLogoutHandler;
import cn.wdy07.server.handler.MessageStoreHandler;
import cn.wdy07.server.handler.MessageTransfer;
import cn.wdy07.server.handler.qualifier.ConversationTypeQualifier;
import cn.wdy07.server.protocol.PrivateProtocolHandler;
import cn.wdy07.server.protocol.Protocol;

/**
 * @author wdy
 * @create 2020-06-21 0:56
 */
public class CPServer {
	public static void main(String[] args) {
		new ServerInitializer()
				// 支持的协议
				.protocol(Protocol.privatee, new PrivateProtocolHandler())

				// 对Message的处理
				// 心跳
				.messageHandler(new ConversationTypeQualifier(ConversationType.HEARTBEAT.ordinal()),
						new HeartBeatHandler())

				// LOGIN、LOGOUT：修改在线客户端、客户端登陆日志
				.messageHandler(
						new ConversationTypeQualifier(
								new int[] { ConversationType.LOGIN.ordinal(), ConversationType.LOGOUT.ordinal() }),
						new LoginLogoutHandler())

				// 私聊、群聊消息历史记录
				.messageHandler(
						new ConversationTypeQualifier(
								new int[] { ConversationType.PRIVATE.ordinal(), ConversationType.GROUP.ordinal() }),
						new MessageStoreHandler())

				// 真正的消息转发处理：多客户端同时在线支持、屏蔽词、已读功能都在这里实现
				.messageHandler(
						new ConversationTypeQualifier(
								new int[] { ConversationType.PRIVATE.ordinal(), ConversationType.GROUP.ordinal() }),
						new MessageTransfer())
				.bind(8080).start();
	}

}
