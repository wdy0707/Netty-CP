package cn.wdy07.server;

import cn.wdy07.constmodle.NetConfig;
import cn.wdy07.server.handler.init.CPServerInitHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wdy
 * @create 2020-06-21 0:56
 */
public class CPServer {

    private static final Logger logger = LoggerFactory.getLogger(CPServer.class);

    public void start(){
        NioEventLoopGroup mainGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup subGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class)
                .group(mainGroup,subGroup)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new CPServerInitHandler());

        try {
            ChannelFuture future = bootstrap.bind(NetConfig.REMOTE_PORT).sync();
            System.out.println("Server started......   "+NetConfig.REMOTE_IP+":"+NetConfig.REMOTE_PORT);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                mainGroup.shutdownGracefully().sync();
                subGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CPServer cpServer = new CPServer();
        cpServer.start();
    }

}
