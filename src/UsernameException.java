
public class UsernameException extends Exception { // Creating own exception to differentiate from the generic exception to be handled
												   // separately.

	private static final long serialVersionUID = 1L;

	public UsernameException() {
		return;
	}

	public UsernameException(String message) {
		super(message);
	}

	public UsernameException(Throwable cause) {
		super(cause);
	}

	public UsernameException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsernameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
