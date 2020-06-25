package cn.wdy07.server;

import cn.wdy07.msgmodel.ConversationType;
import cn.wdy07.msgmodel.Message;
import cn.wdy07.msgmodel.MessageType;
import cn.wdy07.msgmodel.SystemSubType;
import cn.wdy07.server.handler.MessageHandler;
import cn.wdy07.server.handler.business.MessageStoreHandler;
import cn.wdy07.server.handler.function.HeartBeatHandler;
import cn.wdy07.server.protocol.PrivateProtocolHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wdy
 * @create 2020-06-21 0:56
 */
public class CPServer {
    public static void main(String[] args) {
        new ServerInitializer()
        		.protocol("private", new PrivateProtocolHandler())
        		.messageHandler(ConversationType.HEARTBEAT, 0, 0, new HeartBeatHandler())
        		.messageHandler(0, MessageType.CONTENT, 0, new MessageStoreHandler())
        		.messageHandler(0, 0, 0, new MessageHandler() {
					
					@Override
					public void handle(ChannelHandlerContext ctx, Message message) {
						System.out.println(message);
					}
				})
        		.bind(8080)
        		.start();
    }

}
