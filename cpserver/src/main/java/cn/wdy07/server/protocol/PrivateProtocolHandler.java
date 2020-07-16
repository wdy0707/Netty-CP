package cn.wdy07.server.protocol;

import cn.wdy07.model.Message;
import cn.wdy07.serializeframe.factory.SerializerFactory;
import cn.wdy07.serializeframe.intf.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PrivateProtocolHandler implements ProtocolCodec {
	private static final int MAGIC_NUMBER = 0xabcdef;
	private Serializer serializer = SerializerFactory.getSerizalizer(Message.class);
	@Override
	public boolean containsAtLeastOnePacket(ByteBuf buf) {
		int readableBytes = buf.readableBytes();
		
		if (readableBytes < 8)
			return false;
		
		boolean result = true;
		
		buf.markReaderIndex();
		int magicNumber = buf.readInt();
		if (magicNumber != MAGIC_NUMBER)
			result = false;
		
		int packetLen = buf.readInt();
		if (packetLen > readableBytes - 8) 
			result = false;
		
		buf.resetReaderIndex();
		return result;
	}

	@Override
	public Message decode(ByteBuf buf) {
		buf.readInt();
		int packetLen = buf.readInt();
		
        byte[] bytes = new byte[packetLen];
        buf.readBytes(bytes);
        return serializer.deserialize(bytes);

	}

	@Override
	public byte[] encode(Message message) {
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(MAGIC_NUMBER);
		
		byte[] out = serializer.serialize(message);
		buf.writeInt(out.length);
		buf.writeBytes(out);
		int bytes = buf.readableBytes();
		byte[] ret = new byte[bytes];
		buf.readBytes(ret);
		return ret;
	}
}
