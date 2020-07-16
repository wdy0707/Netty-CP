package cn.wdy07.server.user.redis;

public class LockFailureException extends RuntimeException {
	private static final long serialVersionUID = 6610118750265010326L;

	public LockFailureException() {
		super();
	}

	public LockFailureException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LockFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public LockFailureException(String message) {
		super(message);
	}

	public LockFailureException(Throwable cause) {
		super(cause);
	}

}
