package pl.wroc.pwr.judy.general;

import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.ITestResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Immutable container for initial tests run results.
 */
public class TestsRun implements IInitialTestsRun {
	private final List<ITestResult> results;
	private final Map<String, String> inheritance;
	private final ICoverage coverage;
	private long duration;

	/**
	 * Default constructor
	 *
	 * @param results     test results
	 * @param inheritance collected inheritance data
	 * @param coverage    collected test coverage data
	 * @param duration    test run duration in milliseconds
	 */
	public TestsRun(List<ITestResult> results, Map<String, String> inheritance, ICoverage coverage, long duration) {
		this.results = results;
		this.inheritance = inheritance;
		this.coverage = coverage;
		this.duration = duration;
	}

	@Override
	public boolean passed() {
		for (ITestResult test : results) {
			if (!test.passed()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<ITestResult> getPassingResults() {
		List<ITestResult> passingResults = new LinkedList<>();
		for (ITestResult test : results) {
			if (test.passed()) {
				passingResults.add(test);
			}
		}
		return Collections.unmodifiableList(passingResults);
	}

	@Override
	public Map<String, String> getInheritance() {
		return inheritance;
	}

	@Override
	public ICoverage getCoverage() {
		return coverage;
	}

	@Override
	public int getTestsCount() {
		return results.size();
	}

	@Override
	public long getDuration() {
		return duration;
	}
}