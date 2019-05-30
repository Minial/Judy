package pl.wroc.pwr.judy.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.Field;
import java.util.Set;

public class TestThread extends JUnitTestThread {
	private static final Logger LOGGER = LogManager.getLogger(TestThread.class);

	private final Class<?> testClass;
	private final Set<String> testMethods;
	private final DetailedTestResult detailedResult;

	/**
	 * Creates new thread to run loaded test class
	 *
	 * @param testClass      JUnit test class name
	 * @param testMethods    test methods
	 * @param detailedResult test result to be filled during test execution (observer
	 *                       pattern)
	 */
	public TestThread(final Class<?> testClass, final Set<String> testMethods, final DetailedTestResult detailedResult) {
		this.testClass = testClass;
		this.testMethods = testMethods;
		this.detailedResult = detailedResult;
	}

	/**
	 * Runs tests using JUnit core runner if the loaded class is a valid test
	 * class. If not info is shown.
	 *
	 * @return JUnit result or null if class was not found
	 * @throws InterruptedException when thread has been interrupted
	 */
	@Override
	public Result call() throws InterruptedException {
		try {
			JUnitCore runner = createRunner();
			Field field = JUnitCore.class.getDeclaredField("fNotifier");
			field.setAccessible(true);
			RunNotifier runNotifier = (RunNotifier) field.get(runner);

			runner.addListener(detailedResult.getCollector());
			final Result result = runner.run(Request.aClass(testClass).filterWith(new TestMethodFilter(testMethods)));

			runner.removeListener(detailedResult.getCollector());
			runner = null;

			if (Thread.currentThread().isInterrupted()) {
				runNotifier.pleaseStop();
				throw new InterruptedException("Test thread interrupted, JUnit runner asked to stop");
			}
			return result;
		} catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
			logProblemWithFieldAccess(e);
		}
		return null;
	}

	private void logProblemWithFieldAccess(Exception e) {
		LOGGER.debug("Cannot access JUnitCore field while testing: " + testClass, e);
	}
}
