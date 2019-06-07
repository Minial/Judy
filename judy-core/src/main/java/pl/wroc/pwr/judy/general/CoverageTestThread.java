package pl.wroc.pwr.judy.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.loader.TestClassLoader;

public class CoverageTestThread extends JUnitTestThread {
	private static final Logger LOGGER = LogManager.getLogger(CoverageTestThread.class);
	private final String testClass;
	private final String testMethod;
	private final DetailedTestResult detailedResult;
	private final ICoverageClassLoaderFactory loaderFactory;

	/**
	 * Creates new thread to run loaded test class
	 *
	 * @param testClassName  JUnit test class name
	 * @param testMethod     test method
	 * @param detailedResult test result to be filled during test execution (observer
	 *                       pattern)
	 * @param factory        class loader factory
	 */
	public CoverageTestThread(final String testClassName, final String testMethod,
							  final DetailedTestResult detailedResult, final ICoverageClassLoaderFactory factory) {
		testClass = testClassName;
		this.testMethod = testMethod;
		this.detailedResult = detailedResult;
		loaderFactory = factory;
	}

	@Override
	public Result call() {

		try {
			IRuntime runtime = createRuntime();
			Instrumenter instr = createInstrumenter(runtime);
			final RuntimeData runtimeData = createRuntimeData();
			runtime.startup(runtimeData);

			TestClassLoader loader = loaderFactory.createLoader(instr);
			JUnitCore runner = createRunner();
			runner.addListener(detailedResult.getCollector());

			Class<?> loadedTestClass = loader.loadClass(testClass);
			final Result result = runner.run(Request.method(loadedTestClass, testMethod));

			runner.removeListener(detailedResult.getCollector());

			if (result.wasSuccessful()) {
				final ExecutionDataStore executionData = new ExecutionDataStore();
				runtimeData.collect(executionData, new SessionInfoStore(), false);

				final CoverageAnalyzer analyzer = createAnalyzer(executionData, loader);
				analyzer.analyze(testClass, testMethod, executionData);
			}
			//System.out.println(result.wasSuccessful() + " " + testClass + " " + testMethod);

			loadedTestClass = null;
			runner = null;
			loader = null;
			runtime.shutdown();
			instr = null;
			runtime = null;
			return result;
		} catch (final ClassNotFoundException e) {
			LOGGER.debug("Loading test class: " + testClass + " failed", e);
		} catch (final Throwable e) {
			LOGGER.debug("Initializing runtime collecting test execution information failed", e);
		}
		return null;
	}

	protected CoverageAnalyzer createAnalyzer(final ExecutionDataStore executionData, final TestClassLoader loader) {
		return new CoverageAnalyzer(loader.getCache(), loader.getCoverage());
	}

	protected Instrumenter createInstrumenter(final IRuntime runtime) {
		return new Instrumenter(runtime);
	}

	protected RuntimeData createRuntimeData() {
		return new RuntimeData();
	}

	protected IRuntime createRuntime() {
		return new LoggerRuntime();
	}/*
	public String printResult() {
		return result.wasSuccessful() + " " + testClass + " " + testMethod ;
	}*/
}
