package pl.wroc.pwr.judy.common;

/**
 * Common interface for types that have name and description.
 *
 * @author pmiwaszko
 */
public interface IDescriptable {
	/**
	 * Get name.
	 */
	String getName();

	/**
	 * Get description.
	 */
	String getDescription();
}
