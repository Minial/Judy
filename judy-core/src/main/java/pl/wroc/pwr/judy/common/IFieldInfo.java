package pl.wroc.pwr.judy.common;

/**
 * Java field representation.
 *
 * @author pmiwaszko
 */
public interface IFieldInfo {
	/**
	 * Get access of the field.
	 */
	int getAccess();

	/**
	 * Get name of the field.
	 */
	String getName();

	/**
	 * Get description of the field.
	 */
	String getDescription();

	/**
	 * Get signature of the field.
	 */
	String getSignature();

	/**
	 * Get initial value of the static field.
	 */
	Object getValue();

	@Override
	String toString();
}
