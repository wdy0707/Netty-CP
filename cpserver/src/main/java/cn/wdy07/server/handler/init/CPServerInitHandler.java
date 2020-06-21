package cn.wdy07.server.handler.init;

import cn.wdy07.serializeframe.decoder.KryoEncoder;
import cn.wdy07.serializeframe.encoder.KryoDecoder;
import cn.wdy07.server.handler.business.CPSBusinessHandler;
import cn.wdy07.server.handler.function.CPSHeartBeatResHandler;
import cn.wdy07.server.handler.function.CPSLoginAuthResHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author wdy
 * @create 2020-06-21 1:08
 */
public class CPServerInitHandler extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {

        //序列化、反序列化Handler
        ch.pipeline().addLast("KryoDecoder",new KryoDecoder());
        ch.pipeline().addLast("KryoEncoder",new KryoEncoder());

        //超时检测
        ch.pipeline().addLast("ReadTimeoutHandler",new ReadTimeoutHandler(20, TimeUnit.SECONDS));

        //登录授权
        ch.pipeline().addLast("CPSLoginAuthResHandler",new CPSLoginAuthResHandler());

        //心跳
        ch.pipeline().addLast("CPSHeartBeatResHandler",new CPSHeartBeatResHandler());

        //业务处理
        ch.pipeline().addLast("CPSBusinessHandler",new CPSBusinessHandler());
    }
}
