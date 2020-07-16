package cn.wdy07.server.exception;

public class UnAuthorizedTokenException extends RuntimeException {
	private static final long serialVersionUID = -2772946687467809337L;

	public UnAuthorizedTokenException() {
	}

	public UnAuthorizedTokenException(String message) {
		super(message);
	}

	public UnAuthorizedTokenException(Throwable cause) {
		super(cause);
	}

	public UnAuthorizedTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnAuthorizedTokenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
