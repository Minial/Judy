package pl.wroc.pwr.judy.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Run listener collecting information about run test cases in a test class.
 *
 * @author TM
 */
public class TestCaseCollector extends RunListener {
	private List<Description> testcases = new CopyOnWriteArrayList<>();
	private List<Failure> failures = new CopyOnWriteArrayList<>();
	private List<Description> finishedTestcases = new CopyOnWriteArrayList<>();
	private static final Logger LOGGER = LogManager.getLogger(TestCaseCollector.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testStarted(Description description) throws Exception {
		LOGGER.debug("started: " + description.getClassName() + "." + description.getMethodName());
		testcases.add(description);
	}

	@Override
	public void testFinished(Description description) throws Exception {
		LOGGER.debug("finished: " + description.getClassName() + "." + description.getMethodName());
		finishedTestcases.add(description);
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		LOGGER.debug("failed: " + failure.getDescription().getClassName() + "."
				+ failure.getDescription().getMethodName() + " " + failure.getMessage());
		LOGGER.debug(failure.getTrace());
		failures.add(failure);
	}

	/**
	 * @return the test cases
	 */
	public List<Description> getTestcases() {
		return testcases;
	}

	/**
	 * @return the failures
	 */
	public List<Failure> getFailures() {
		List<Failure> failedTestcases = getInterruptedTestcases();
		failedTestcases.addAll(failures);
		return failedTestcases;
	}

	private List<Failure> getInterruptedTestcases() {
		getInterruptedDescriptions();

		ArrayList<Failure> interrupted = new ArrayList<>();
		for (Description description : getInterruptedDescriptions()) {
			interrupted.add(new Failure(description, new InterruptedException()));
		}
		interrupted.trimToSize();
		return interrupted;
	}

	private List<Description> getInterruptedDescriptions() {
		ArrayList<Description> interruptedDescriptions = new ArrayList<>();
		interruptedDescriptions.addAll(testcases);
		interruptedDescriptions.removeAll(finishedTestcases);
		interruptedDescriptions.trimToSize();
		return interruptedDescriptions;
	}
}
