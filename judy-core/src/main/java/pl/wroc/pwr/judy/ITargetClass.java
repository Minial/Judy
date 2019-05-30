package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.work.TestDuration;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Representation of class to be mutated.
 *
 * @author pmiwaszko
 */
public interface ITargetClass extends Serializable {
	/**
	 * Get package name of class, e.g. <code>pl.wroc.pwr.judy.Example</code>.
	 *
	 * @return test class name with package
	 */
	String getName();

	/**
	 * Get list of tests classes names covering this class.
	 *
	 * @return list of test classes names
	 */
	Collection<String> getCoveringTestClasses();

	/**
	 * Return number of test classes covering this class
	 *
	 * @return test classes count
	 */
	int getCoveringTestClassesCount();

	/**
	 * Gets collection of results of initial test runs covering this class.
	 *
	 * @return list of initial test runs
	 */
	Collection<ITestResult> getInitialTestResults();

	/**
	 * Returns effort index factor, based on coverage and tests size
	 *
	 * @return effort index
	 */
	float getEffort();

	/**
	 * Get names of test methods which are covering given mutated lines
	 *
	 * @param testClass    test class name
	 * @param mutatedLines mutated lines
	 * @return test method names
	 */
	Set<String> getCoveringMethods(String testClass, List<Integer> mutatedLines);

	/**
	 * Gets test classes with information about their execution duration
	 *
	 * @return test duration information
	 */
	List<TestDuration> getSortedTests();
}
