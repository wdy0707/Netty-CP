package cn.wdy07.server.handler.function;

import cn.wdy07.model.Message;
import cn.wdy07.model.MsgHeader;
import cn.wdy07.model.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wdy
 * @create 2020-06-21 1:22
 */
//暂时只做IP白名单验证，后续拓展拦截请求
public class CPSLoginAuthResHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger logger = LoggerFactory.getLogger(CPSLoginAuthResHandler.class);

    // 存储用户登录状态（需要线程安全？）
    private Set<String> loginStatusSet = new HashSet<>();

    //IP白名单
    private Set<String> writeIPSet = new HashSet<>();
    {
        writeIPSet.add("127.0.0.1");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        //判断消息类型，对登录请求进行判断
        if(msg.getHeader() != null && MsgType.LOGIN_REQ.getValue() == msg.getHeader().getType()){
            String remoteAddress = ctx.channel().remoteAddress().toString();
            Message loginRes = null;
            if(loginStatusSet.contains(remoteAddress)){
                //重复登录
                loginRes = buildResponse(MsgType.LOGIN_RESP,(byte)-1);
            }else{
                //检查白名单，是否允许登录
                String hostAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
                if(writeIPSet.contains(hostAddress)){
                    //允许登录
                    loginRes = buildResponse(MsgType.LOGIN_RESP,(byte)0);
                    loginStatusSet.add(remoteAddress);
                }else{
                    loginRes = buildResponse(MsgType.LOGIN_RESP,(byte)-1);
                }
            }
            logger.info("Login response: "+loginRes);
            //直接返回
            ctx.writeAndFlush(loginRes);
            ReferenceCountUtil.release(msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private Message buildResponse(MsgType msgType,byte info) {
        Message message = new Message();
        MsgHeader header = new MsgHeader();
        header.setType(msgType.getValue());
        message.setHeader(header);
        message.setBody(info);
        return message;
    }

    //channel出现异常，关闭连接，并删除登录状态
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        loginStatusSet.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
    }
}
