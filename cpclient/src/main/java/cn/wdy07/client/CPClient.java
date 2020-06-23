package cn.wdy07.client;

import cn.wdy07.client.init.CPClientInitHandler;
import cn.wdy07.constmodle.NetConfig;
import cn.wdy07.msgmodel.Message;
import cn.wdy07.msgmodel.MsgHeader;
import cn.wdy07.msgmodel.MsgType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wdy
 * @create 2020-06-21 13:37
 */
public class CPClient implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(CPClient.class);

    private final String IP;
    private final int port;

    public CPClient(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    // 因为存在断连重连，在属性中定义
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final EventLoopGroup group = new NioEventLoopGroup();

    //是否用户关闭连接
    private volatile boolean userClose = false;
    //是否连接成功
    private volatile boolean connected = false;
    public boolean isConnected(){

        return connected;
    }

    //重试次数
    private final AtomicInteger retryCount = new AtomicInteger();

    //连接Channel
    private Channel channel;

    public void connect(){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .remoteAddress(new InetSocketAddress(IP,port))
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new CPClientInitHandler());

        try {
            ChannelFuture future = bootstrap.connect().sync();
            channel = future.sync().channel();
            //连接成功，获得Channel，通知等待线程
            synchronized (this){
                connected = true;
                notifyAll();
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            // 连接断开，进行重连
            if(!userClose){
                // 非用户关闭，进行重连
                logger.error("连接发生异常！下面进行重连 ---->");
                executorService.execute(this::reConnect);
            }else{
                //用户主动关闭，释放资源
                try {
                    channel = null;
                    group.shutdownGracefully().sync();
                    synchronized (this){
                        connected = false;
                        notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reConnect(){
        try {
            logger.error("进行第 "+retryCount.incrementAndGet()+" 次重连......");
            TimeUnit.SECONDS.sleep(2);
            connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        connect();
    }

    //对外调用
    public void sengMsg(String content){
        if(channel == null || !channel.isActive()){
            throw new IllegalStateException("连接还未建立，请稍后再试！");
        }

        Message msg = new Message();
        MsgHeader header = new MsgHeader();
        header.setType(MsgType.SERVICE_REQ.getValue());
        msg.setHeader(header);
        msg.setBody(content);
        channel.writeAndFlush(msg);
    }

    public void close() {
        userClose = true;
        channel.close();
    }

    //测试
    public static void main(String[] args) {
        CPClient cpClient = new CPClient(NetConfig.REMOTE_IP, NetConfig.REMOTE_PORT);
        cpClient.connect();
    }

}
