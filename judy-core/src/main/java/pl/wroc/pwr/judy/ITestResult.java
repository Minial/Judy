package pl.wroc.pwr.judy;

import java.util.List;
import java.util.Set;

/**
 * Unit test result interface.
 */
public interface ITestResult {
	/**
	 * @return package class name of unit test.
	 */
	String getClassName();

	/**
	 * @return <code>true</code> if it test passed, <code>false</code>
	 * otherwise.
	 */
	boolean passed();

	/**
	 * @return duration of test run
	 */
	long getDuration();

	/**
	 * @return exceptions throws during test run.
	 */
	List<Throwable> getThrownExceptions();

	/**
	 * @return set of all test method names from test class
	 */
	Set<String> getTestMethods();

	/**
	 * @return set of all failing test methods
	 */
	Set<String> getFailingTestMethods();

	/**
	 * @return set of all failing test methods, failed because of infinite loop
	 * guard
	 */
	Set<String> getFailingByInfiniteLoopGuardTestMethods();

	/**
	 * @return set of all successfully executed test methods
	 */
	Set<String> getSuccessfulTestMethods();
}
