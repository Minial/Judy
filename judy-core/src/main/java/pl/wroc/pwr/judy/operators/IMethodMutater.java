package pl.wroc.pwr.judy.operators;

/**
 * Common interface for all method mutaters.
 *
 * @author pmiwaszko
 */
public interface IMethodMutater {
	/**
	 * Get line number last seen by this method mutater.
	 */
	int getLastLineNumber();
}
