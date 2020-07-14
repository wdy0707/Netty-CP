package cn.wdy07.server.user.token;

public class AllPassTokenChecker implements TokenChecker<Token> {

	@Override
	public boolean check(Token wrapper) {
		return true;
	}

}
