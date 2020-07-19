package cn.wdy07.server.user.login;

public class UnAllowedLoginStrategyException extends RuntimeException {
	private static final long serialVersionUID = 1637973840538350311L;

	public UnAllowedLoginStrategyException() {
		super();
	}

	public UnAllowedLoginStrategyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnAllowedLoginStrategyException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnAllowedLoginStrategyException(String message) {
		super(message);
	}

	public UnAllowedLoginStrategyException(Throwable cause) {
		super(cause);
	}
}
