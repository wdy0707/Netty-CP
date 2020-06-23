package cn.wdy07.client.function;

import cn.wdy07.client.CPClient;
import cn.wdy07.msgmodel.Message;
import cn.wdy07.msgmodel.MsgHeader;
import cn.wdy07.msgmodel.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wdy
 * @create 2020-06-21 15:31
 */
public class CPCHeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CPClient.class);

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        //登录成功，发送心跳
        if(message.getHeader()!=null
                && message.getHeader().getType() == MsgType.LOGIN_RESP.getValue()) {
            //发送心跳
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new CPCHeartBeatReqHandler.HeartBeatTask(ctx), 0,
                    5000, TimeUnit.MILLISECONDS);

            ReferenceCountUtil.release(msg);
        }else if(message.getHeader()!=null
                && message.getHeader().getType() == MsgType.HEARTBEAT_RESP.getValue()){
            logger.info("收到服务端心跳应答："+message);
            ReferenceCountUtil.release(msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    //心跳请求任务
    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;
        //心跳计数
        private final AtomicInteger heartBeatCount;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
            heartBeatCount = new AtomicInteger();
        }

        @Override
        public void run() {
            Message heartBeat = buildHeatBeat();
            logger.info("客户端第 "+heartBeatCount.incrementAndGet()+" 次"+"发送心跳: " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private Message buildHeatBeat() {
            Message message = new Message();
            MsgHeader header = new MsgHeader();
            header.setType(MsgType.HEARTBEAT_REQ.getValue());
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

}
