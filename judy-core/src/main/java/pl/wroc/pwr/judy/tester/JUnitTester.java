package pl.wroc.pwr.judy.tester;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.general.JUnitTestResult;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class JUnitTester {
	private static final Logger LOGGER = LogManager.getLogger(JUnitTester.class);
	public static final long TEST_TIMEOUT = 12000;

	private ExecutorService executor;

	/**
	 * Creates JUnit tester with limited threads count
	 *
	 * @param executor threads executor
	 */
	public JUnitTester(ExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * Creates test run result
	 *
	 * @param testClassName test class name
	 * @param results       test execution results data
	 * @param duration      test run duration
	 * @param exceptions    exception thrown during test execution
	 * @return test run result
	 */
	public JUnitTestResult createRunResult(String testClassName, DetailedTestResult results, long duration,
										   List<Throwable> exceptions) {
		DetailedTestResult finalResults = results == null ? new DetailedTestResult() : results;
		return new JUnitTestResult(testClassName, exceptions.isEmpty() && finalResults.wasSuccessfull(), duration,
				exceptions, finalResults);
	}

	/**
	 * Factory method creating thread executor
	 *
	 * @return executor service
	 */
	public ExecutorService getExecutor() {
		return executor;
	}

	/**
	 * Handles timeouts of threads for executed test methods
	 *
	 * @param futureTasks test threads for every test method
	 * @param timeout     test method thread timeout
	 * @return list of exceptions thrown during specified timeout
	 */
	public List<Exception> handleTimeouts(List<TestTask> futureTasks, long timeout) {
		ArrayList<Exception> exceptions = new ArrayList<>();
		for (TestTask task : futureTasks) {
			exceptions.addAll(handleTimeout(timeout, task));
		}
		exceptions.trimToSize();
		return exceptions;
	}

	/**
	 * Handles timeout of thread executing test methods
	 *
	 * @param task    test thread
	 * @param timeout test thread timeout
	 * @return list of exceptions thrown during specified timeout
	 */
	public List<Exception> handleTimeout(long timeout, TestTask task) {
		ArrayList<Exception> exceptions = new ArrayList<>();
		try {
			task.getTask().get(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOGGER.debug(task.getDescription() + " interrupted", e);
			exceptions.add(e);
		} catch (ExecutionException e) {
			LOGGER.debug(task.getDescription() + " execution failed", e);
			exceptions.add(e);
		} catch (TimeoutException e) {
			exceptions.add(e);
			LOGGER.debug(task.getDescription() + " timed out");
		} catch (Exception e) {
			exceptions.add(e);
			LOGGER.debug(task.getDescription() + " failed with exception", e);
		} finally {
			LOGGER.debug(task.getDescription() + " cancelled=" + task.getTask().cancel(true));
		}
		exceptions.trimToSize();
		return exceptions;
	}

	/**
	 * Shutdowns thread pool executor.
	 */
	public void shutdown() {
		System.gc();
	}
}