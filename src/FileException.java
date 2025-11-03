
public class FileException extends Exception { // Creating own exception to differentiate from the generic exception to be handled
												   // separately.

	private static final long serialVersionUID = 1L;

	public FileException() {
		return;
	}

	public FileException(String message) {
		super(message);
	}

	public FileException(Throwable cause) {
		super(cause);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
