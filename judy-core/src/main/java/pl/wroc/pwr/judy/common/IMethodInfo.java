package pl.wroc.pwr.judy.common;

import java.util.List;

/**
 * Java method representation.
 *
 * @author pmiwaszko
 */
public interface IMethodInfo {
	/**
	 * Get access of the method.
	 */
	int getAccess();

	/**
	 * Get name of the method.
	 */
	String getName();

	/**
	 * Get description of the method.
	 */
	String getDesc();

	/**
	 * Get signature of the method.
	 */
	String getSignature();

	/**
	 * Get list of exceptions thrown by the method.
	 */
	List<String> getExceptions();

	// TODO owner would be useful

	@Override
	String toString();
}
