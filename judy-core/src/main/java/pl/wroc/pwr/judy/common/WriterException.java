package pl.wroc.pwr.judy.common;

/**
 * Exception indicating problem during saving mutation result.
 *
 * @author pmiwaszko
 */
public class WriterException extends Exception {
	private static final long serialVersionUID = 180L;

	/**
	 * <code>WriterException</code> constructor.
	 */
	public WriterException(String message) {
		super(message);
	}

	/**
	 * <code>WriterException</code> constructor.
	 */
	public WriterException(Throwable cause) {
		super(cause);
	}
}
