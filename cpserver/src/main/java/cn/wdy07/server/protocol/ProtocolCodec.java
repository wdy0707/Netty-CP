package cn.wdy07.server.protocol;

import cn.wdy07.model.Message;
import io.netty.buffer.ByteBuf;

public interface ProtocolCodec {
	boolean containsAtLeastOnePacket(ByteBuf buf);
	Message decode(ByteBuf buf);
	
	byte[] encode(Message message);
	
}
