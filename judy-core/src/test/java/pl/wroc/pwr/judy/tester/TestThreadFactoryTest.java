package pl.wroc.pwr.judy.tester;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.general.CoverageTestThread;
import pl.wroc.pwr.judy.general.TestThread;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class TestThreadFactoryTest {

	private static final Set<String> TEST_METHOD_NAMES = new HashSet<>();
	private static final String TEST_CLASS_NAME = "testClassName";
	private static final String TEST_METHOD_NAME = "testMethodName";
	private TestThreadFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new TestThreadFactory();
	}

	@Test
	public void shouldCreateMutationTestThread() throws Exception {
		TestThread thread = factory.createMutationTestThread(null, this.getClass(), TEST_METHOD_NAMES);
		assertNotNull(thread);
	}

	@Test
	public void shouldCreateCoverageTestThread() throws Exception {
		CoverageTestThread thread = factory.createCoverageTestThread(null, null, TEST_CLASS_NAME, TEST_METHOD_NAME);
		assertNotNull(thread);
	}

}
