package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;

import static org.junit.Assert.assertNotNull;

public class JUnitTestThreadTest {
	private JUnitTestThread thread;

	@Before
	public void setUp() {
		thread = new JUnitTestThread() {
			@Override
			public Result call() throws Exception {
				return null;
			}
		};
	}

	@Test
	public void shouldCreateJunitCoreRunner() throws Exception {
		assertNotNull(thread.createRunner());
	}
}
