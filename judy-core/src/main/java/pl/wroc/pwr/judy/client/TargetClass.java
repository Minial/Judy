package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.work.TestDuration;
import pl.wroc.pwr.judy.work.TestDurationComparator;

import java.util.*;

/**
 * Implementation of {@link ITargetClass} interface.
 *
 * @author pmiwaszko
 */
class TargetClass implements ITargetClass {
	private static final long serialVersionUID = 320L;

	private final String name;
	private final float effort;
	private IInitialTestsRun initialTests;

	/**
	 * <code>TargetClass</code> constructor.
	 */
	public TargetClass(String name, float effort, IInitialTestsRun initialTests) {
		this.name = name;
		this.effort = effort;
		this.initialTests = initialTests;
	}

	@Override
	public Collection<String> getCoveringTestClasses() {
		return Collections.unmodifiableCollection(initialTests.getCoverage().getCoveringTestClasses(name));
	}

	@Override
	public Set<String> getCoveringMethods(String testClass, List<Integer> mutatedLines) {
		return initialTests.getCoverage().getCoveringTestMethods(name, testClass, mutatedLines);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getEffort() {
		return effort;
	}

	@Override
	public Collection<ITestResult> getInitialTestResults() {
		ArrayList<ITestResult> results = new ArrayList<>();
		Set<String> coverage = initialTests.getCoverage().getCoveringTestClasses(name);
		for (ITestResult result : initialTests.getPassingResults()) {
			if (coverage.contains(result.getClassName())) {
				results.add(result);
			}
		}
		results.trimToSize();
		return results;
	}

	@Override
	public List<TestDuration> getSortedTests() {
		List<TestDuration> tests = new LinkedList<>();
		for (ITestResult result : getInitialTestResults()) {
			tests.add(new TestDuration(result.getClassName(), result.getDuration()));
		}
		Collections.sort(tests, new TestDurationComparator());
		return tests;
	}

	@Override
	public int getCoveringTestClassesCount() {
		return getCoveringTestClasses().size();
	}
}
