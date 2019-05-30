package pl.wroc.pwr.judy.common;

/**
 * Mutation point is a place in bytecode where one or more mutations can be
 * applied.
 *
 * @author pmiwaszko
 */
public interface IMutationPoint {
	/**
	 * Get index of the mutation point.
	 */
	int getIndex();

	/**
	 * Get number of possible mutations in the mutation point. Any non-negative
	 * number (including 0) is possible.
	 */
	int getMutantsCount();
}
