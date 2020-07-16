package cn.wdy07.server.exception;

public class UserUnLoggedInException extends RuntimeException {
	private static final long serialVersionUID = 4181658778817738528L;

	public UserUnLoggedInException() {
		super();
	}

	public UserUnLoggedInException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserUnLoggedInException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserUnLoggedInException(String message) {
		super(message);
	}

	public UserUnLoggedInException(Throwable cause) {
		super(cause);
	}
}
