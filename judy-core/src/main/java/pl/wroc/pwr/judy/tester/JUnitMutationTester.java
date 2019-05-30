package pl.wroc.pwr.judy.tester;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class JUnitMutationTester extends JUnitTester implements ITester {
	private static final Logger LOGGER = LogManager.getLogger(JUnitMutationTester.class);

	private ITestThreadFactory threadFactory;
	public MatrixExecution MatrixE;

	/**
	 * Creates JUnit mutation tester with given test thread factory.
	 *
	 * @param threadFactory test thread factory
	 * @param executor      threads executor
	 */
	public JUnitMutationTester(ITestThreadFactory threadFactory, ExecutorService executor, MatrixExecution MatrixE) {
		super(executor);
		this.threadFactory = threadFactory;
		this.MatrixE = MatrixE;
	}

	@Override
	public ITestResult getTestResult(String testClassName, Set<String> testMethods, long timeout) {
		LOGGER.debug("Collecting test results for: " + testClassName);
		List<Throwable> exceptions = new LinkedList<>();
		DetailedTestResult results = getResult(testClassName, testMethods, timeout, exceptions);
		return createRunResult(testClassName, results, timeout, exceptions);
	}

	private DetailedTestResult getResult(String testClassName, Set<String> testMethods, long timeout,
										 List<Throwable> exceptions) {
		DetailedTestResult details = new DetailedTestResult();
		try {
			executeTests(testClassName, testMethods, exceptions, details, timeout);
		} catch (Exception e) {
			LOGGER.debug("Exception during test execution: " + testClassName, e);
			exceptions.add(e);
		}
		return details;
	}

	protected void executeTests(String testClassName, Set<String> testMethods, List<Throwable> exceptions,
								DetailedTestResult details, long timeout) {
		ClassLoader mutationLoader = Thread.currentThread().getContextClassLoader();
		ExecutorService executor = getExecutor();
		try {
			TestTask futureTask = submitTestMethods(mutationLoader.loadClass(testClassName), testMethods, details,
					executor);
			exceptions.addAll(handleTimeout(timeout, futureTask));
		} catch (ClassNotFoundException e) {
			LOGGER.debug("Test class could not be loaded: " + testClassName);
		}
	}

	private TestTask submitTestMethods(Class<?> testClass, Set<String> testMethods, DetailedTestResult details,
									   ExecutorService service) {
		return new TestTask(testClass.getName(), testMethods.size(), service.submit(threadFactory
				.createMutationTestThread(details, testClass, testMethods)));
	}
}
