package cn.wdy07.server.handler.function;

import cn.wdy07.msgmodel.Message;
import cn.wdy07.msgmodel.MsgHeader;
import cn.wdy07.msgmodel.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wdy
 * @create 2020-06-21 13:13
 */
public class CPSHeartBeatResHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CPSHeartBeatResHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 前面已经反序列化
        Message message = (Message) msg;
        //判断是否为心跳报文，并处理
        if(message.getHeader() != null
                && message.getHeader().getType() == MsgType.HEARTBEAT_REQ.getValue()){

            logger.info("Server accept HeartBeatRequest: "+message);
            Message heartBeatRes = buildHeatBeat();
            ctx.writeAndFlush(heartBeatRes);
            logger.info("server sent HeartBeatResponse: "+heartBeatRes);

        }else{
            ctx.fireChannelRead(msg);
        }
    }

    // 构建心跳报文
    private Message buildHeatBeat() {
        Message message = new Message();
        MsgHeader header = new MsgHeader();
        header.setType(MsgType.HEARTBEAT_RESP.getValue());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
