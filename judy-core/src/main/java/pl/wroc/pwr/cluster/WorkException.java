package pl.wroc.pwr.cluster;

/**
 * Exception indicating problem during distributed work computations.
 *
 * @author pmiwaszko
 */
public class WorkException extends Exception {
	private static final long serialVersionUID = 100L;

	/**
	 * <code>WorkException</code> constructor.
	 */
	public WorkException(Throwable cause) {
		super(cause);
	}

	/**
	 * <code>WorkException</code> constructor.
	 */
	public WorkException(String message) {
		super(message);
	}
}
