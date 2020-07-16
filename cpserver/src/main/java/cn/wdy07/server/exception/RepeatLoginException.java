package cn.wdy07.server.exception;

public class RepeatLoginException extends RuntimeException {
	public RepeatLoginException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -5678976790490826553L;
}
