package pl.wroc.pwr.judy.tester;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.general.CoverageTestThread;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.utils.Timer;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class JUnitCoverageTester extends JUnitTester implements ITester {
	private static final Logger LOGGER = LogManager.getLogger(JUnitCoverageTester.class);
	private ITestThreadFactory threadFactory;
	private ICoverageClassLoaderFactory classLoaderFactory;
	private MatrixCoverage MatrixC;

	/**
	 * Creates JUnit mutation tester with given test thread factory.
	 *
	 * @param threadFactory test thread factory
	 * @param executor      threads executor
	 */
	public JUnitCoverageTester(ICoverageClassLoaderFactory classLoaderFactory, ITestThreadFactory threadFactory, ExecutorService executor, MatrixCoverage MatrixC) {
		super(executor);
		this.threadFactory = threadFactory;
		this.classLoaderFactory = classLoaderFactory;
		this.MatrixC = MatrixC;
	}

	@Override
	public ITestResult getTestResult(String testClassName, Set<String> testMethods, long timeout) {
		LOGGER.debug("Collecting test results for: " + testClassName);
		Timer timer = new Timer();
		List<Throwable> exceptions = new LinkedList<>();
		DetailedTestResult results = runTestCoverage(testClassName, testMethods, classLoaderFactory, timeout,
				exceptions);//matrice de couverture ici ?
		return createRunResult(testClassName, results, timer.getDuration(), exceptions);//return a JUnitTestResult
	}

	private DetailedTestResult runTestCoverage(String testClassName, Set<String> testMethods,
											   ICoverageClassLoaderFactory factory, long timeout, List<Throwable> exceptions) {
		DetailedTestResult details = new DetailedTestResult();
		ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
		try {
			executeTests(testClassName, testMethods, factory, exceptions, details);
		} catch (Exception e) {
			LOGGER.debug("Exception during test execution", e);
			exceptions.add(e);
		} finally {
			Thread.currentThread().setContextClassLoader(contextLoader);
		}
		return details;//matrice d'execution ici ?
	}

	protected void executeTests(String testClassName, Set<String> testMethods, ICoverageClassLoaderFactory factory,
								List<Throwable> exceptions, DetailedTestResult details) {
		Thread.currentThread().setContextClassLoader(factory.createLoader());
		ExecutorService executor = getExecutor();

		List<TestTask> futureTasks = submitTestMethodsTasks(testClassName, testMethods, factory, details, executor);
		exceptions.addAll(handleTimeouts(futureTasks, JUnitTester.TEST_TIMEOUT));
	}

	private List<TestTask> submitTestMethodsTasks(String testClassName, Set<String> testMethods,
												  ICoverageClassLoaderFactory factory, DetailedTestResult details, ExecutorService executor) {
		List<TestTask> futureTasks = new ArrayList<>(testMethods.size());

		for (String testMethod : testMethods) {
			CoverageTestThread task = threadFactory.createCoverageTestThread(factory, details, testClassName,
					testMethod);
			futureTasks.add(new TestTask(testClassName, testMethod, executor.submit(task)));
		}
		return futureTasks;
	}
}
