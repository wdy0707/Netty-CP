package cn.wdy07.server.user.token;

import cn.wdy07.model.Message;

public class TokenCheckManager<T extends Token> {
	TokenChecker<? super T> checker;
	TokenConverter<T> converter;
	public TokenCheckManager(TokenChecker<? super T> checker, TokenConverter<T> converter) {
		this.checker = checker;
		this.converter = converter;
	}
	
	public boolean check(Message message) {
		return checker.check(converter.convert(message));
	}
}
