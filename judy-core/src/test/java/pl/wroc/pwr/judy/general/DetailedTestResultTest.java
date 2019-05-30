package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DetailedTestResultTest {
	private DetailedTestResult cut;
	private TestCaseCollector collector;

	@Before
	public void setUp() {
		cut = new DetailedTestResult();
		collector = Mockito.mock(TestCaseCollector.class);
	}

	@Test
	public void shouldInitializeWithDefaultCollector() throws Exception {
		assertNotNull(cut.getCollector());
		assertTrue(cut.wasSuccessfull());
	}

	@Test
	public void shouldChangeCollector() throws Exception {
		assertNotNull(cut.getCollector());

		cut.setCollector(null);
		assertNull(cut.getCollector());
	}

	@Test
	public void shouldBeSuccessfullWithNoTestcasesRunAndCollected() throws Exception {
		checkIfIsSuccessfull(true, new ArrayList<Description>(), new ArrayList<Failure>());
	}

	@Test
	public void shouldBeSuccessfullWithFewTestcasesRunAndCollected() throws Exception {
		Description description = Mockito.mock(Description.class);
		checkIfIsSuccessfull(true, Arrays.asList(description, description, description), new ArrayList<Failure>());
	}

	@Test
	public void shouldNotBeSuccessfullWithAnyFailure() throws Exception {
		Failure failure = Mockito.mock(Failure.class);
		checkIfIsSuccessfull(false, new ArrayList<Description>(), Arrays.asList(failure, failure, failure));
	}

	@Test
	public void shouldGetCollectorTestcases() throws Exception {
		List<Description> testMethods = new ArrayList<>();
		mockTestCollector(testMethods, null);
		assertEquals(testMethods, cut.getTestMethods());
	}

	@Test
	public void shouldGetCollectorFailures() throws Exception {
		List<Failure> failures = new ArrayList<>();
		mockTestCollector(null, failures);
		assertEquals(failures, cut.getFailures());
	}

	private void checkIfIsSuccessfull(boolean expectedResult, List<Description> testcases, List<Failure> failures) {
		mockTestCollector(testcases, failures);
		assertEquals(expectedResult, cut.wasSuccessfull());
	}

	private void mockTestCollector(List<Description> testMethods, List<Failure> failures) {
		Mockito.when(collector.getTestcases()).thenReturn(testMethods);
		Mockito.when(collector.getFailures()).thenReturn(failures);
		cut.setCollector(collector);
	}
}
