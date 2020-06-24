package cn.wdy07.server.protocol;

import java.util.List;

import cn.wdy07.msgmodel.Message;
import io.netty.buffer.ByteBuf;

public interface ProtocolHanlder {
	boolean containsAtLeastOnePacket(ByteBuf buf);
	List<Message> decode(ByteBuf buf);
	
	byte[] encode(Message message);
	
}
