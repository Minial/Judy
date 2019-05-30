package pl.wroc.pwr.judy.general;

import pl.wroc.pwr.judy.ITestResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TestResultList extends ArrayList<ITestResult> {
	private static final long serialVersionUID = 1L;

	/**
	 * Gets unique failing test methods
	 *
	 * @return failing test methods
	 */
	public Set<String> getFailingTestMethods() {
		Set<String> methods = new HashSet<>();
		for (ITestResult testResult : this) {
			methods.addAll(testResult.getFailingTestMethods());
		}
		return methods;
	}

	/**
	 * Gets unique failing test methods, failed because of Judy's Infinite Loop
	 * Guard
	 *
	 * @return failing test methods
	 */
	public Set<String> getFailingTestMethodsBecauseOfInfiniteLoopGuard() {
		Set<String> methods = new HashSet<>();
		for (ITestResult testResult : this) {
			methods.addAll(testResult.getFailingByInfiniteLoopGuardTestMethods());
		}
		return methods;
	}

	/**
	 * Gets all unique test methods
	 *
	 * @return test methods
	 */
	public Set<String> getTestMethods() {
		Set<String> methods = new HashSet<>();
		for (ITestResult testResult : this) {
			methods.addAll(testResult.getTestMethods());
		}
		return methods;
	}
}

