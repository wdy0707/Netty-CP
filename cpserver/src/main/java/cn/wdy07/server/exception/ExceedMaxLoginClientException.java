package cn.wdy07.server.exception;

public class ExceedMaxLoginClientException extends RuntimeException {
	private static final long serialVersionUID = -414412583379994347L;

	public ExceedMaxLoginClientException(String message) {
		super(message);
	}
	
	
}
