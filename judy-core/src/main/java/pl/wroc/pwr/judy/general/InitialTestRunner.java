package pl.wroc.pwr.judy.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.junit.JUnitTestFinder;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.IInitialTestsRunner;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.loader.CoverageClassLoaderFactory;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.loader.TestClassLoader;
import pl.wroc.pwr.judy.tester.ITester;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.tester.JUnitTester;
import pl.wroc.pwr.judy.utils.BytecodeCache;
import pl.wroc.pwr.judy.utils.Timer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of initial run and analysis of given unit tests.
 *
 * @author pmiwaszko
 */
public class InitialTestRunner implements IInitialTestsRunner {
	private static final Logger LOGGER = LogManager.getLogger(InitialTestRunner.class);
	private final List<String> testClasses;
	private final ITesterFactory testerFactory;
	private final IBytecodeCache cache;

	/**
	 * <code>InitialTestRunner</code> constructor.
	 */
	public InitialTestRunner(String workspace, List<String> testClasses, List<String> classpath,
							 ITesterFactory testerFactory) {
		this.testClasses = testClasses;
		this.testerFactory = testerFactory;
		cache = new BytecodeCache(workspace, classpath);
	}

	@Override
	public IInitialTestsRun run() throws MutationException {
		Timer initialTestTimer = new Timer();
		final List<ITestResult> results = new LinkedList<>();
		ICoverage coverage = new MapCoverage();
		final Map<String, String> inheritance = new HashMap<>();

		if (testClasses.isEmpty()) {
			throw new MutationException("No tests were found.");
		}

		for (String testClassName : testClasses) {
			LOGGER.debug("Checking potential test class: " + testClassName);
			ICoverageClassLoaderFactory classLoaderFactory = createClassLoaderFactory(inheritance, coverage,
					testClassName);
			TestClassLoader loader = classLoaderFactory.createLoader();
			ITester tester = testerFactory.createCoverageTester(classLoaderFactory);

			try {
				Class<?> loadedClass = loader.loadClass(testClassName);
				if (JUnitTestFinder.isJUnitTest(loadedClass)) {
					LOGGER.debug("Running JUnit test class: " + testClassName);
					ITestResult result = tester.getTestResult(testClassName,
							TestCasesFinder.findTestMethods(loadedClass), JUnitTester.TEST_TIMEOUT);
					if (result.passed()) {
						results.add(result);
					}
				} else {
					LOGGER.debug("Not a JUnit test class, skipping: " + testClassName);
				}
			} catch (ClassNotFoundException e) {
				LOGGER.debug("Test class " + testClassName + " not found", e);
			} catch (NoClassDefFoundError e) {
				LOGGER.debug("Test class definition " + testClassName + " not found", e);
			}
			loader = null;
		}
		return new TestsRun(results, inheritance, coverage, initialTestTimer.getDuration());
	}

	@Override
	public IBytecodeCache getBytecodeCache() {
		return cache;
	}

	protected ICoverageClassLoaderFactory createClassLoaderFactory(Map<String, String> inheritance, ICoverage coverage,
																   String name) {
		return new CoverageClassLoaderFactory(name, cache, coverage, inheritance);
	}
}
