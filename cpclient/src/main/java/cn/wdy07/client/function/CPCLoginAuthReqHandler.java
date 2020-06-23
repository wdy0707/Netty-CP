package cn.wdy07.client.function;

import cn.wdy07.client.CPClient;
import cn.wdy07.msgmodel.Message;
import cn.wdy07.msgmodel.MsgHeader;
import cn.wdy07.msgmodel.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wdy
 * @create 2020-06-21 15:15
 */
public class CPCLoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CPClient.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连接成功，直接登录
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Message message = (Message) msg;
        //判断是否登录成功
        if(message.getHeader() != null
                && message.getHeader().getType() == MsgType.LOGIN_RESP.getValue()){
            byte loginRes = (byte)message.getBody();
            if(loginRes != (byte)0){
                //登录失败
                logger.error("登陆失败！");
                ctx.close();
            }else {
                System.out.println("Login successfully ~~~~~~~"+message);
                // 登陆成功后，发送心跳
                ctx.fireChannelRead(msg);
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private Message buildLoginReq() {
        Message message = new Message();
        MsgHeader header = new MsgHeader();
        header.setType(MsgType.LOGIN_REQ.getValue());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
