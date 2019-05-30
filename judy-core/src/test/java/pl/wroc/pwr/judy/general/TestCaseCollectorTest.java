package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCaseCollectorTest {

	private TestCaseCollector collector;

	@Before
	public void setUp() throws Exception {
		collector = new TestCaseCollector();
	}

	@Test
	public void shouldSaveStartedTest() throws Exception {
		assertTrue(collector.getTestcases().isEmpty());

		collector.testStarted(Mockito.mock(Description.class));

		assertEquals(1, collector.getTestcases().size());
	}

	@Test
	public void shouldSaveFailedTest() throws Exception {
		assertTrue(collector.getTestcases().isEmpty());

		collector.testFailure(new Failure(Mockito.mock(Description.class), new Exception()));

		assertEquals(1, collector.getFailures().size());
	}

	@Test
	public void shouldSaveInterruptedMethodAsFailure() throws Exception {
		Description methodA = Mockito.mock(Description.class);
		Description methodB = Mockito.mock(Description.class);
		collector.testStarted(methodA);
		collector.testStarted(methodB);
		collector.testFinished(methodA);
		assertEquals(1, collector.getFailures().size());
	}

	@Test
	public void shouldCombineFailuresAndInterruptedMethods() throws Exception {
		collector.testStarted(Mockito.mock(Description.class));
		collector.testFailure(new Failure(Mockito.mock(Description.class), new NullPointerException()));

		assertEquals(2, collector.getFailures().size());
		assertTrue(collector.getFailures().get(0).getException() instanceof InterruptedException);
		assertTrue(collector.getFailures().get(1).getException() instanceof NullPointerException);
	}

}
