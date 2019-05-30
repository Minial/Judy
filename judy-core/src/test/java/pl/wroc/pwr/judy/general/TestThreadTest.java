package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestThreadTest {
	private static final String TEST_METHOD = "testMethod";
	private Set<String> testMethods;
	private TestThread thread;
	private DetailedTestResult detailedResult;
	private JUnitCore runner;

	@Before
	public void setUp() throws Exception {
		detailedResult = new DetailedTestResult();

		testMethods = new HashSet<>();
		testMethods.add(TEST_METHOD);

		runner = mockTestRunAndAddingListener();
		thread = mockJunitRunnerInitialization();
	}

	@Test
	public void shouldRunTestMethodWithCollectorAsListener() throws Exception {
		thread.call();
		assertEquals(1, detailedResult.getTestMethods().size());
	}

	private JUnitCore mockTestRunAndAddingListener() {
		return new JUnitCore() {
			@Override
			public Result run(Request request) {
				try {
					detailedResult.getCollector().testStarted(Mockito.mock(Description.class));
				} catch (Exception e) {
				}
				return null;
			}

			@Override
			public void addListener(RunListener listener) {
				super.addListener(listener);
				assertEquals(detailedResult.getCollector(), listener);
			}
		};
	}

	private TestThread mockJunitRunnerInitialization() {
		return new TestThread(null, testMethods, detailedResult) {
			@Override
			public JUnitCore createRunner() {
				return runner;
			}
		};
	}
}
