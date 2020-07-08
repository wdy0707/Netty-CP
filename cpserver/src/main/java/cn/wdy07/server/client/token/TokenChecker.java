package cn.wdy07.server.client.token;

import cn.wdy07.model.Message;

/**
 * 客户端登陆时，应用服务器为不同客户端给予token，连接通信服务器时，通信服务器校验该token
 * 通信服务器向应用服务器询问token
 * 
 * @author taylor
 *
 */
public interface TokenChecker<T extends Token> {
	
	// 检查该token是否合法
	boolean check(T token);
}
