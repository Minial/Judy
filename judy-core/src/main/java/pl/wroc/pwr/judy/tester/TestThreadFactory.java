package pl.wroc.pwr.judy.tester;

import pl.wroc.pwr.judy.general.CoverageTestThread;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.general.TestThread;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.MatrixExecution;
import pl.wroc.pwr.judy.MatrixCoverage;

import java.util.Set;

public class TestThreadFactory implements ITestThreadFactory {

	@Override
	public CoverageTestThread createCoverageTestThread(ICoverageClassLoaderFactory factory, DetailedTestResult details,
													   String testClass, String testMethod) {
		return new CoverageTestThread(testClass, testMethod, details, factory);
	}

	@Override
	public TestThread createMutationTestThread(DetailedTestResult details, Class<?> testClass, Set<String> testMethods) {
		return new TestThread(testClass, testMethods, details);
	}
}
