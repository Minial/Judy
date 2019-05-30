package pl.wroc.pwr.judy.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.ExecutionDataStore;
import pl.wroc.pwr.judy.common.IBytecodeCache;

import java.io.IOException;

public class CoverageAnalyzer {
	private IBytecodeCache cache;
	private ICoverage coverage;

	static final Logger LOGGER = LogManager.getLogger(CoverageAnalyzer.class);

	/**
	 * Creates coverage analyzer for given test method.
	 *
	 * @param cache    bytecode cache with source and test classes
	 * @param coverage global coverage data
	 */
	public CoverageAnalyzer(IBytecodeCache cache, ICoverage coverage) {
		this.cache = cache;
		this.coverage = coverage;
	}

	/**
	 * Analyzes code coverage of test method in test class. Then coverage data
	 * (about invoked methods of source classes) is saved in passed Coverage
	 * instance.
	 *
	 * @param testClass     test class name
	 * @param testMethod    test method name
	 * @param executionData test method execution data
	 */
	public void analyze(String testClass, String testMethod, ExecutionDataStore executionData) {
		final CoverageBuilder coverageBuilder = createBuilder();
		final Analyzer analyzer = createAnalyzer(executionData, coverageBuilder);
		analyzeSourceClasses(testClass, analyzer);
		saveCoverage(testClass, testMethod, coverageBuilder);
	}

	/**
	 * Analyzes source classes covered by given test class using JaCoCo analyzer
	 *
	 * @param analyzer jacoco coverage analyzer
	 */
	private void analyzeSourceClasses(String testClass, Analyzer analyzer) {
		for (String sourceClass : coverage.getCoveredClasses(testClass)) {
			if (cache.get(sourceClass) != null) {
				try {
					analyzer.analyzeClass(cache.get(sourceClass), sourceClass);
				} catch (IOException e) {
					LOGGER.info(e);
				}
			}
		}
	}

	/**
	 * Saves coverage data (lines covered by given test method) in Coverage
	 * instance.
	 *
	 * @param testClass       test class name
	 * @param coverageBuilder JaCoCo coverage builder
	 */
	private void saveCoverage(String testClass, String testMethod, final CoverageBuilder coverageBuilder) {
		for (final IClassCoverage cc : coverageBuilder.getClasses()) {
			MethodCoverage mc = getCoveredLines(testMethod, cc);
			coverage.addMethod(getClassName(cc), testClass, mc);
		}
	}

	private String getClassName(final IClassCoverage cc) {
		return cc.getName().replace('/', '.');
	}

	protected Analyzer createAnalyzer(ExecutionDataStore executionData, final CoverageBuilder coverageBuilder) {
		return new Analyzer(executionData, coverageBuilder);
	}

	protected CoverageBuilder createBuilder() {
		return new CoverageBuilder();
	}

	private MethodCoverage getCoveredLines(String testMethod, final IClassCoverage cc) {
		MethodCoverage mc = new MethodCoverage(testMethod);
		for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
			if (isLineCovered(cc.getLine(i))) {
				mc.addLine(i);
			}
		}
		mc.trimSize();
		return mc;
	}

	private boolean isLineCovered(ILine line) {
		return line.getStatus() == ICounter.FULLY_COVERED || line.getStatus() == ICounter.PARTLY_COVERED;
	}
}
