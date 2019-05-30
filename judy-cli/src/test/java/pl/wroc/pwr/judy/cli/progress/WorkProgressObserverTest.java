package pl.wroc.pwr.judy.cli.progress;

import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.ITargetClass;

import static org.junit.Assert.assertEquals;

public class WorkProgressObserverTest {

	@Test
	public void testCreateMessage() {
		final String summary = " this is summary text";

		final ITargetClass targetClass = Mockito.mock(ITargetClass.class);
		Mockito.when(targetClass.getEffort()).thenReturn(1f, 30f, 40f);

		final IClassMutationResult result = Mockito.mock(IClassMutationResult.class);
		Mockito.when(result.getSummary()).thenReturn(summary);
		Mockito.when(result.getTargetClass()).thenReturn(targetClass);

		final WorkProgressObserver observer = new WorkProgressObserver();

		assertEquals("[  1.0%] this is summary text", observer.createMessage(result));
		assertEquals("[ 31.0%] this is summary text", observer.createMessage(result));
		assertEquals("[ 71.0%] this is summary text", observer.createMessage(result));
	}

}
