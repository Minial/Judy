package pl.wroc.pwr.judy.tester;

import org.junit.runner.Result;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.general.JUnitTestThread;

import java.util.concurrent.*;

public abstract class AbstractJUnitTesterTest {

	private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

	@SuppressWarnings("unchecked")
	public Future<Result> mockTaskWithException(Class<? extends Throwable> exception, long timeout)
			throws InterruptedException, ExecutionException, TimeoutException {
		Future<Result> timeoutedTask = Mockito.mock(Future.class);
		Mockito.when(timeoutedTask.get(timeout, TimeUnit.MILLISECONDS)).thenThrow(exception);
		return timeoutedTask;
	}

	public void verifyTasksExecution(Future<?> task1, Future<?> task2, JUnitTestThread thread, ExecutorService service,
									 long timeout) throws InterruptedException, ExecutionException, TimeoutException {
		Mockito.verify(service, Mockito.times(2)).submit(thread);
		Mockito.verify(task1).get(timeout, TIME_UNIT);
		Mockito.verify(task2).get(timeout, TIME_UNIT);
	}

	public void verifyTaskExecution(Future<?> task1, JUnitTestThread thread, ExecutorService service, long timeout)
			throws InterruptedException, ExecutionException, TimeoutException {
		Mockito.verify(service).submit(thread);
		Mockito.verify(task1).get(timeout, TIME_UNIT);
	}
}
