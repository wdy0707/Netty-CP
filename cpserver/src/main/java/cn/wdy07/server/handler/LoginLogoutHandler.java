package cn.wdy07.server.handler;

import java.util.List;

import cn.wdy07.model.Message;
import cn.wdy07.model.content.LoginMessageContent;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.server.client.ClientManager;
import cn.wdy07.server.client.InMemeoryClientManager;
import cn.wdy07.server.exception.ExceedMaxLoginClientException;
import cn.wdy07.server.exception.RepeatLoginException;
import cn.wdy07.server.exception.UnAuthorizedTokenException;
import cn.wdy07.server.handler.offline.InMemoryOfflineMessageManager;
import cn.wdy07.server.handler.offline.OfflineMessageManager;
import cn.wdy07.server.protocol.message.MessageBuilder;
import io.netty.channel.ChannelHandlerContext;

public class LoginLogoutHandler implements MessageHandler {
	ClientManager clientManager = InMemeoryClientManager.getInstance();
	OfflineMessageManager offlineMessageManager = InMemoryOfflineMessageManager.getInstance();

	@Override
	public void handle(ChannelHandlerContext ctx, Message message) {
		if (message.getHeader().getConversationType() == ConversationType.LOGIN) {
			Message retMessage;
			try {
				clientManager.login(message, ctx.channel());
				
				// TODO: 构建OK报文
				LoginMessageContent content = new LoginMessageContent();
				content.setCode(200);
				content.setText("login success");
				retMessage = MessageBuilder.create()
						.convesationType(ConversationType.LOGIN)
						.userId("system")
						.targetId(message.getHeader().getUserId())
						.content(content)
						.build();
				
				ctx.channel().writeAndFlush(retMessage);
				// TODO: 获取历史消息
				
				List<Message> offlineMessages = offlineMessageManager.getOfflineMessage(message.getHeader().getUserId());
				for (Message message2 : offlineMessages)
					ctx.channel().writeAndFlush(message2);
				
			} catch (RepeatLoginException e) {
				// TODO: 构建重复登陆报文
			} catch (ExceedMaxLoginClientException e2) {
				// TODO: 构建超过登陆设备报文
			} catch (UnAuthorizedTokenException e) {
				// TODO: 构建token不正确报文
			}
		} else { // logout
			clientManager.logout(message, ctx.channel());
		}
			
	}
}
