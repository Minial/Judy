package pl.wroc.pwr.judy.general;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import java.util.List;

/**
 * Container for detailed test run result with collected test methods and
 * failures.
 *
 * @author TM
 */
public class DetailedTestResult {
	private TestCaseCollector collector;

	/**
	 * Default constructor, creates details with empty result and test case
	 * collector
	 */
	public DetailedTestResult() {
		collector = new TestCaseCollector();
	}

	/**
	 * @return the collector
	 */
	public TestCaseCollector getCollector() {
		return collector;
	}

	/**
	 * @param collector the cache to set
	 */
	public void setCollector(TestCaseCollector collector) {
		this.collector = collector;
	}

	/**
	 * Determines if test execution was successful.
	 *
	 * @return true if there were 0 or more testcases run without any failure.
	 */
	public boolean wasSuccessfull() {
		return collector.getTestcases().size() >= 0 && collector.getFailures().isEmpty();
	}

	/**
	 * Gets test failures from collector
	 *
	 * @return test failures
	 */
	public List<Failure> getFailures() {
		return collector.getFailures();
	}

	/**
	 * Gets test methods from collector
	 *
	 * @return test methods
	 */
	public List<Description> getTestMethods() {
		return collector.getTestcases();
	}
}
