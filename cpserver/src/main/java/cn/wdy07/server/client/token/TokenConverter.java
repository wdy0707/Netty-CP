package cn.wdy07.server.client.token;

import cn.wdy07.model.Message;

public interface TokenConverter<T extends Token> {
	
	T convert(Message message);
	
}
