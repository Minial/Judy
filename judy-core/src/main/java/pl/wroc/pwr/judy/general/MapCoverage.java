package pl.wroc.pwr.judy.general;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default coverage implementation based on Map structure
 *
 * @author TM
 */
public class MapCoverage implements ICoverage {
	private final Map<String, Set<TestCoverage>> coverage = new ConcurrentHashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addClass(String sourceClass, String testClass) {
		if (!getCoverageMap().containsKey(sourceClass)) {
			getCoverageMap().put(sourceClass, new HashSet<TestCoverage>());
		}
		getTestCoverage(sourceClass).add(new TestCoverage(testClass));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Set<String> getCoveredClasses(String testClass) {
		Set<String> coveredClasses = new HashSet<>();

		for (Entry<String, Set<TestCoverage>> coverageEntry : getCoverageMap().entrySet()) {
			for (TestCoverage tc : coverageEntry.getValue()) {
				if (tc.getTestClassName().equals(testClass)) {
					coveredClasses.add(coverageEntry.getKey());
				}
			}
		}
		return coveredClasses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Set<String> getCoveringTestClasses(String sourceClass) {
		Set<String> coveringClasses = new HashSet<>();
		for (TestCoverage tc : getTestCoverage(sourceClass)) {
			coveringClasses.add(tc.getTestClassName());
		}
		return coveringClasses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCovered(String sourceClass) {
		return !getTestCoverage(sourceClass).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Set<String> getCoveringTestMethods(String sourceClass, String testClass, List<Integer> lines) {
		Set<String> methods = new HashSet<>();
		for (MethodCoverage mc : getMethodCoverage(sourceClass, testClass)) {
			for (Integer line : lines) {
				if (mc.getCoveredLines().contains(line)) {
					methods.add(mc.getMethodName());
				}
			}
		}
		return methods;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addMethod(String sourceClass, String testClass, MethodCoverage methodCoverage) {
		TestCoverage testCoverage = getSingleTestCoverage(sourceClass, testClass);
		if (testCoverage != null) {
			testCoverage.addMethod(methodCoverage);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int countCoveringTestClasses(String sourceClass) {
		return getTestCoverage(sourceClass).size();
	}

	private TestCoverage getSingleTestCoverage(String sourceClass, String testClass) {
		for (TestCoverage tc : getTestCoverage(sourceClass)) {
			if (tc.getTestClassName().equals(testClass)) {
				return tc;
			}
		}
		return null;
	}

	private Set<TestCoverage> getTestCoverage(String sourceClass) {
		Set<TestCoverage> testCoverage = getCoverageMap().get(sourceClass);
		return testCoverage == null ? new HashSet<TestCoverage>() : testCoverage;
	}

	private Set<MethodCoverage> getMethodCoverage(String sourceClass, String testClass) {
		for (TestCoverage tc : getTestCoverage(sourceClass)) {
			if (tc.getTestClassName().equals(testClass)) {
				return tc.getMethodCoverage();
			}
		}
		return new HashSet<>();
	}

	/**
	 * Gets data structure containing coverage information. Used for testing
	 * purposes only.
	 *
	 * @return coverage data map
	 */
	protected Map<String, Set<TestCoverage>> getCoverageMap() {
		return coverage;
	}
}
