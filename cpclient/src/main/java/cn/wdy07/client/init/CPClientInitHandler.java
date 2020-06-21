package cn.wdy07.client.init;

import cn.wdy07.client.function.CPCHeartBeatReqHandler;
import cn.wdy07.client.function.CPCLoginAuthReqHandler;
import cn.wdy07.serializeframe.decoder.KryoEncoder;
import cn.wdy07.serializeframe.encoder.KryoDecoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author wdy
 * @create 2020-06-21 14:07
 */
public class CPClientInitHandler extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {

        //序列化、反序列化
        ch.pipeline().addLast("KryoDecoder",new KryoDecoder());
        ch.pipeline().addLast("KryoEncoder",new KryoEncoder());

        //超时检测
        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(10));

        //登录授权
        ch.pipeline().addLast("CPCLoginAuthReqHandler",new CPCLoginAuthReqHandler());

        //心跳
        ch.pipeline().addLast("CPCHeartBeatReqHandler",new CPCHeartBeatReqHandler());

    }
}
