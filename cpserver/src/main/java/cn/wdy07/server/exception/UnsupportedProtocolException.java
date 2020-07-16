package cn.wdy07.server.exception;

public class UnsupportedProtocolException extends RuntimeException {

	private static final long serialVersionUID = -157955100987169912L;

	public UnsupportedProtocolException() {
		super();
	}

	public UnsupportedProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedProtocolException(String message) {
		super(message);
	}

	public UnsupportedProtocolException(Throwable cause) {
		super(cause);
	}



}
