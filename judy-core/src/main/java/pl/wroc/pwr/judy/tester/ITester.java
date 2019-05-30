package pl.wroc.pwr.judy.tester;

import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;

import java.util.Set;

/**
 * Common interface for all testers. Tester is able to run unit tests.
 *
 * @author pmiwaszko
 */
public interface ITester {

	/**
	 * Run given unit test and get detailed information about its result.
	 *
	 * @param testClassName test class name
	 * @param testMethods   test methods to run
	 * @param timeout       maximum test execution duration
	 * @return test result information
	 */
	ITestResult getTestResult(String testClassName, Set<String> testMethods, long timeout);

	/**
	 * Performs shutdown on thread executor service
	 */
	void shutdown();
}
