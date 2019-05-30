package pl.wroc.pwr.judy.tester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;
import org.mockito.Mockito;

import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class TestTaskTest {
	private static String METHOD_NAME = "methodName";
	private static String CLASS_NAME = "className";
	private TestTask task;
	private Future<Result> future;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		future = Mockito.mock(Future.class);
		task = new TestTask(CLASS_NAME, METHOD_NAME, future);
	}

	@Test
	public void shouldReturnFullTestMethodName() throws Exception {
		assertEquals(CLASS_NAME + "." + METHOD_NAME, task.getDescription());
	}

	@Test
	public void shouldReturnFutureTask() throws Exception {
		assertEquals(future, task.getTask());
	}

}
