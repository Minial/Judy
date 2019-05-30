package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.general.ICoverage;

import java.util.List;
import java.util.Map;

/**
 * Representation of initial tests result.
 */
public interface IInitialTestsRun {
	/**
	 * @return true if it all tests passed, false otherwise.
	 */
	boolean passed();

	/**
	 * @return results of individual, passed tests
	 */
	List<ITestResult> getPassingResults();

	/**
	 * Get information about inheritance hierarchy between loaded target
	 * classes.
	 *
	 * @return mappings of class name to its super class name
	 */
	Map<String, String> getInheritance();

	/**
	 * Get information about tests coverage.
	 *
	 * @return mappings of class name to list of names of covering test classes
	 */
	ICoverage getCoverage();

	/**
	 * Gets initial tests count, including failed test classes
	 *
	 * @return used test classes count
	 */
	int getTestsCount();

	/**
	 * Gets initial test run duration
	 *
	 * @return duration in milliseconds
	 */
	long getDuration();
}