package pl.wroc.pwr.judy.general;

import java.util.List;
import java.util.Set;

/**
 * Interface for object handling test class and test methods coverage data for
 * source classes
 *
 * @author TM
 */
public interface ICoverage {

	/**
	 * Adds test class coverage for given source class (class under tests)
	 *
	 * @param sourceClass source class name
	 * @param testClass   test class name
	 */
	void addClass(String sourceClass, String testClass);

	/**
	 * Gets source classes covered by given test class
	 *
	 * @param testClass test class name
	 * @return covered source classes
	 */
	Set<String> getCoveredClasses(String testClass);

	/**
	 * Gets test classes covering given source class (class under tests)
	 *
	 * @param sourceClass source class name
	 * @return covering test classes
	 */
	Set<String> getCoveringTestClasses(String sourceClass);

	/**
	 * Gets number of test classes covering given source class (class under
	 * test)
	 *
	 * @param sourceClass source class name
	 * @return number of covering test classes
	 */
	int countCoveringTestClasses(String sourceClass);

	/**
	 * Checks if given source class is covered by any test class
	 *
	 * @param sourceClass source class name
	 * @return true if there is at least one covering test class, false
	 * otherwise
	 */
	boolean isCovered(String sourceClass);

	/**
	 * Gets test class' method names covering specified lines in source class.
	 * To be included method must cover at least one of specified lines in
	 * source class.
	 *
	 * @param sourceClass sourceClass
	 * @param testClass   test class name
	 * @param lines       line numbers
	 * @return test methods
	 */
	Set<String> getCoveringTestMethods(String sourceClass, String testClass, List<Integer> lines);

	/**
	 * Adds method coverage information for of test class method for given
	 * source class
	 *
	 * @param sourceClass    source class name
	 * @param testClass      test class name
	 * @param methodCoverage method coverage information
	 */
	void addMethod(String sourceClass, String testClass, MethodCoverage methodCoverage);
}
