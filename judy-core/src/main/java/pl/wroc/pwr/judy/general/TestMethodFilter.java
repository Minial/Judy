package pl.wroc.pwr.judy.general;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import java.util.Set;

public class TestMethodFilter extends Filter {
	private static final String DESCRIPTION = "Test method filter for running only selected methods";
	private Set<String> testMethods;

	/**
	 * Creates test method filter
	 *
	 * @param testMethods test method names
	 */
	public TestMethodFilter(Set<String> testMethods) {
		this.testMethods = testMethods;
	}

	@Override
	public boolean shouldRun(Description description) {
		return testMethods.contains(description.getMethodName());
	}

	@Override
	public String describe() {
		return DESCRIPTION;
	}

}
