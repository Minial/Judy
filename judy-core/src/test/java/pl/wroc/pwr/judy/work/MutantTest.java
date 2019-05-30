package pl.wroc.pwr.judy.work;

import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITestResult;

import static org.junit.Assert.*;

public class MutantTest {

	@Test
	public void shouldSaveTestResults() throws Exception {
		Mutant m = new Mutant("", 0, 0, "", 0, 0);
		assertNotNull(m.getResults());
		assertTrue(m.getResults().isEmpty());

		ITestResult result = Mockito.mock(ITestResult.class);
		m.saveResult(result);

		assertEquals(1, m.getResults().size());
	}
}
