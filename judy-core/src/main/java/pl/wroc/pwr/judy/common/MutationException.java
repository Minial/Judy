package pl.wroc.pwr.judy.common;

/**
 * Exception indicating problem during mutation analysis process.
 *
 * @author pmiwaszko
 */
public class MutationException extends Exception {
	private static final long serialVersionUID = 165L;

	/**
	 * <code>MutationException</code> constructor.
	 */
	public MutationException(String message) {
		super(message);
	}

	/**
	 * <code>MutationException</code> constructor.
	 */
	public MutationException(Throwable cause) {
		super(cause);
	}
}
