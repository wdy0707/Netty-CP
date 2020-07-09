package cn.wdy07.server.client.token;

import cn.wdy07.model.Message;
import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.model.header.ClientType;

public class SimpleTokenConverter implements TokenConverter<SimpleToken> {

	public SimpleTokenConverter() {

	}

	@Override
	public SimpleToken convert(Message message) {

		SimpleToken token = new SimpleToken();
		token.setToken(((LoginRequestMessageContent) message.getContent()).getToken());
		token.setType(((LoginRequestMessageContent) message.getContent()).getClientType());
		token.setUserId(message.getHeader().getUserId());
		return token;
	}
}
