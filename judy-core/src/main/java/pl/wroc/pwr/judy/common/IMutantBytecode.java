package pl.wroc.pwr.judy.common;

/**
 * Mutated version of bytecode.
 *
 * @author pmiwaszko
 */
public interface IMutantBytecode {
	/**
	 * Get mutated bytecode.
	 */
	byte[] getBytecode();

	/**
	 * Get line number in which mutation was introduced.
	 */
	int getLineNumber();

	/**
	 * Get descriptive details of mutant. Might be <code>null</code>.
	 */
	String getDescription();

	/**
	 * Get optional method descriptor being mutated
	 *
	 * @author Michal Borek
	 */
	String getMethodDescriptor();

	/**
	 * Get optional method name being mutated
	 *
	 * @author Michal Borek
	 */
	String getMethodName();
}
