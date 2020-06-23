package cn.wdy07.serializeframe.encoder;

import cn.wdy07.msgmodel.Message;
import cn.wdy07.serializeframe.factory.SerializerFactory;
import cn.wdy07.serializeframe.intf.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author wdy
 * @create 2020-06-21 0:06
 */
//不使用LengthFieldBasedFrameDecoder，采用手动解码的方式，便于后续扩展兼容多种协议
public class KryoDecoder extends ByteToMessageDecoder {

    //消息头添加Msg大小，用于切分
    private static final int HEADLENGTH = 4;

    private Serializer serializer = SerializerFactory.getSerizalizer(Message.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //首先进行能够解析判断
        if(in.readableBytes() <= HEADLENGTH) return;

        //用于后面判断失败重置
        in.markReaderIndex();

        int msgLength = in.readInt();
        if(in.readableBytes() < msgLength){
            // 消息不完整，半包，重置index，直接返回等待后续读取
            in.resetReaderIndex();
            return;
        }

        //进行反序列化
        byte[] bytes = new byte[msgLength];
        in.readBytes(bytes);
        Object deObj = serializer.deserialize(bytes);
        out.add(deObj);
    }
}
