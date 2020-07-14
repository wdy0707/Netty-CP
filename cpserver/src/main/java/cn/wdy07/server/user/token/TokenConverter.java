package cn.wdy07.server.user.token;

import cn.wdy07.model.Message;

public interface TokenConverter<T extends Token> {
	
	T convert(Message message);
	
}
