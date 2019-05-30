package pl.wroc.pwr.judy.general;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.util.concurrent.Callable;

public abstract class JUnitTestThread implements Callable<Result> {

	/**
	 * Creates JUnit test runner
	 *
	 * @return runner
	 */
	public JUnitCore createRunner() {
		JUnitCore runner = new JUnitCore();
		return runner;
	}

}
