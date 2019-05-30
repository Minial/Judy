package pl.wroc.pwr.judy.operators;

/**
 * Common interface for all class mutaters.
 *
 * @author pmiwaszko
 */
public interface IClassMutater {
	/**
	 * Get number of the first mutated line.
	 */
	int getMutantLineNumber();

	/**
	 * Get optional description of performed mutation.
	 */
	String getMutantDescription();

	/**
	 * Get optional method descriptor being mutated.
	 */
	String getMethodDescriptor();

	/**
	 * Get optional method name being mutated.
	 */
	String getMethodName();
}
