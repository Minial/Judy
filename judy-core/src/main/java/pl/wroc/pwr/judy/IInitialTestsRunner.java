package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.MutationException;

/**
 * Interface encapsulating initial tests run. This run is performed on client
 * side as the mutation analysis can be performed only on a passing set of unit
 * tests. Initial tests run is used to collect information about classes
 * hierarchy and tests coverage too.
 *
 * @author pmiwaszko
 */
public interface IInitialTestsRunner {
	/**
	 * Run initial tests analysis, including running all test, collecting
	 * inheritance and coverage information.
	 *
	 * @return result of initial test run
	 * @throws MutationException if exception while running initial tests occurs
	 */
	IInitialTestsRun run() throws MutationException;

	/**
	 * @return cache used during initial testing.
	 */
	IBytecodeCache getBytecodeCache();
}
