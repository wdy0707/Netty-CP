package cn.wdy07.serializeframe.decoder;

import cn.wdy07.msgmodel.Message;
import cn.wdy07.serializeframe.factory.SerializerFactory;
import cn.wdy07.serializeframe.intf.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author wdy
 * @create 2020-06-21 0:09
 */
//不使用LengthFieldBasedFrameDecoder，采用手动解码的方式，便于后续扩展兼容多种协议
public class KryoEncoder extends MessageToByteEncoder<Message> {

    private Serializer serializer = SerializerFactory.getSerizalizer(Message.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {

        byte[] bytes = serializer.serialize(msg);
        //需要将消息长度写入二进制消息流头部，用于Decode
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
