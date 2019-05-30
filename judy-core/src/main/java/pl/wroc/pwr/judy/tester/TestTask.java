package pl.wroc.pwr.judy.tester;

import org.junit.runner.Result;

import java.util.concurrent.Future;

/**
 * Class holding future tasks of single JUnit test method run
 *
 * @author tmuc
 */
public class TestTask {
	private String description;
	private Future<Result> task;

	/**
	 * Creates test task for single test method
	 *
	 * @param testClassName  test class name
	 * @param testMethodName test method name
	 * @param task           future task
	 */
	public TestTask(String testClassName, String testMethodName, Future<Result> task) {
		description = testClassName + "." + testMethodName;
		this.task = task;
	}

	/**
	 * Creates test task for set of test methods
	 *
	 * @param testClassName test class name
	 * @param testMethods   test method names
	 * @param task          future task
	 */
	public TestTask(String testClassName, int testMethods, Future<Result> task) {
		description = testClassName + " (tests: " + testMethods + ")";
		this.task = task;
	}

	/**
	 * @return the task
	 */
	public Future<Result> getTask() {
		return task;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
}
