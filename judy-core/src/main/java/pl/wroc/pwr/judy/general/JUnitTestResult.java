package pl.wroc.pwr.judy.general;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Result of JUnit test.
 */
public class JUnitTestResult implements ITestResult {
	private final String name;
	private final boolean passed;
	private final long duration;
	private final List<Throwable> exceptions;
	private DetailedTestResult details;

	/**
	 * Default constructor.
	 *
	 * @param name       test class name
	 * @param passed     whether there were no exceptions or not
	 * @param duration   test run duration in milliseconds
	 * @param exceptions list of exceptions thrown during initialization and
	 *                   preparation of test run
	 * @param details    detailed test results
	 */
	public JUnitTestResult(String name, boolean passed, long duration, List<Throwable> exceptions,
						   DetailedTestResult details) {
		this.name = name;
		this.passed = passed;
		this.duration = duration;
		this.exceptions = exceptions;
		this.details = details;
	}

	@Override
	public String getClassName() {
		return name;
	}

	@Override
	public long getDuration() {
		return duration;
	}

	@Override
	public boolean passed() {
		return passed;
	}

	@Override
	public List<Throwable> getThrownExceptions() {
		ArrayList<Throwable> throwables = new ArrayList<>();
		for (Failure failure : details.getFailures()) {
			throwables.add(failure.getException());
		}
		throwables.addAll(exceptions);
		throwables.trimToSize();
		return throwables;
	}

	private Set<String> getMethods(List<Description> descriptions) {
		Set<String> testcases = new HashSet<>();
		for (Description description : descriptions) {
			testcases.add(description.getClassName() + "." + description.getMethodName());
		}
		return testcases;
	}

	@Override
	public Set<String> getTestMethods() {
		return getMethods(details.getTestMethods());
	}

	@Override
	public Set<String> getFailingTestMethods() {
		Set<String> failedTestcases = new HashSet<>();
		for (Failure failure : details.getFailures()) {
			failedTestcases.add(failure.getDescription().getClassName() + "."
					+ failure.getDescription().getMethodName());
		}
		return failedTestcases;
	}

	@Override
	public Set<String> getFailingByInfiniteLoopGuardTestMethods() {
		Set<String> failedBecauseOfInfiniteLoop = new HashSet<>();
		for (Failure failure : details.getFailures()) {
			if (failure.getException() instanceof InfiniteLoopException) {
				failedBecauseOfInfiniteLoop.add(failure.getDescription().getClassName() + "."
						+ failure.getDescription().getMethodName());
			}
		}
		return failedBecauseOfInfiniteLoop;
	}

	@Override
	public Set<String> getSuccessfulTestMethods() {
		Set<String> successfulTestcases = new HashSet<>();
		successfulTestcases.addAll(getTestMethods());
		successfulTestcases.removeAll(getFailingTestMethods());
		return successfulTestcases;
	}
}
