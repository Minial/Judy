package pl.wroc.pwr.judy.tester;

import pl.wroc.pwr.judy.general.CoverageTestThread;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.general.TestThread;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;

import java.util.Set;

public interface ITestThreadFactory {
	/**
	 * Creates test thread for running specified test method of a test class
	 *
	 * @param details     test results container
	 * @param testClass   test class name
	 * @param testMethods test method names
	 * @return JUnit test thread
	 */
	TestThread createMutationTestThread(DetailedTestResult details, Class<?> testClass, Set<String> testMethods);

	/**
	 * @param factory    coverage class loader factory
	 * @param details    test results container
	 * @param testClass  test class name
	 * @param testMethod test method name
	 * @return JUnit coverage test thread
	 */
	CoverageTestThread createCoverageTestThread(ICoverageClassLoaderFactory factory, DetailedTestResult details,
												String testClass, String testMethod);
}
