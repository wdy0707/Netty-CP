package cn.wdy07.server.handler;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.model.Protocol;
import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.model.content.LoginResponseMessageContent;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.exception.UnAuthorizedTokenException;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.protocol.SupportedProtocol;
import cn.wdy07.server.protocol.message.MessageBuilder;
import cn.wdy07.server.protocol.message.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public class LoginLogoutHandler implements MessageHandler {
	ClientManager clientManager = InMemeoryClientManager.getInstance();
	OfflineMessageManager offlineMessageManager = InMemoryOfflineMessageManager.getInstance();

	@Override
	public void handle(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		Message message = wrapper.getMessage();
		String userId = message.getHeader().getUserId();
		boolean logined = false;
		if (message.getHeader().getConversationType() == ConversationType.LOGIN) {
			Message retMessage = null;
			try {
				clientManager.login(wrapper, ctx.channel());
				logined = true;

				// TODO: 构建OK报文
				LoginResponseMessageContent content = new LoginResponseMessageContent();
				content.setCode(200);
				content.setContent("login success");
				retMessage = MessageBuilder.create().convesationType(ConversationType.LOGIN).userId("system")
						.targetId(wrapper.getMessage().getHeader().getUserId()).content(content).build();
			} catch (RepeatLoginException e) {
				// TODO: 构建重复登陆报文
			} catch (ExceedMaxLoginClientException e2) {
				// TODO: 构建超过登陆设备报文
			} catch (UnAuthorizedTokenException e3) {
				// TODO: 构建token不正确报文
			} catch (Throwable t) {

			}

			MessageWrapper out = new MessageWrapper(retMessage);
			out.addAllDescription(wrapper);
			Protocol protocol = SupportedProtocol.getInstance().getOneSupportedProtocol(
					((LoginRequestMessageContent) message.getContent()).getSupportedProtocols());
			out.addDescription(MessageWrapper.receiverKey, message.getHeader().getUserId());
			out.addDescription(MessageWrapper.protocolKey, protocol);

			ctx.channel().writeAndFlush(out);

			if (logined) {
				// TODO: 获取历史消息

				// 离线消息发送
				List<MessageWrapper> offlineMessages = offlineMessageManager
						.getOfflineMessage(userId);
				for (MessageWrapper wrapper2 : offlineMessages) {
					wrapper2.addDescription(MessageWrapper.protocolKey, protocol);
					ctx.channel().writeAndFlush(wrapper2);
				}
			}

		} else { // logout
			clientManager.logout(wrapper, ctx.channel());
		}

	}
}
