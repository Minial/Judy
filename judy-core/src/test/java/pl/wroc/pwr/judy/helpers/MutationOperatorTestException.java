package pl.wroc.pwr.judy.helpers;

/**
 * Custom exception type thrown in tests of mutation operators.
 *
 * @author pmiwaszko
 */
public class MutationOperatorTestException extends RuntimeException {
	private static final long serialVersionUID = -455596771878575011L;

	/**
	 * <code>MutationOperatorTestException</code> constructor.
	 */
	public MutationOperatorTestException(String message) {
		super(message);
	}

	/**
	 * <code>MutationOperatorTestException</code> constructor.
	 */
	public MutationOperatorTestException(String message, Throwable cause) {
		super(message, cause);
	}
}
