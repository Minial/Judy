package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCoverageTest {

	private static final String TEST_CLASS_NAME = "testClassName";
	private TestCoverage tc;

	@Before
	public void setUp() throws Exception {
		tc = new TestCoverage(TEST_CLASS_NAME);
	}

	@Test
	public void shouldReturnClassName() throws Exception {
		assertEquals(TEST_CLASS_NAME, tc.getTestClassName());
	}

	@Test
	public void shouldAddCoveredMethodsWithLines() throws Exception {
		assertTrue(tc.getMethodCoverage().isEmpty());

		MethodCoverage m1 = new MethodCoverage("method1");
		MethodCoverage m2 = new MethodCoverage("method2");
		String duplicatedMethod = "method3";
		MethodCoverage m3 = new MethodCoverage(duplicatedMethod, Arrays.asList(1, 2));
		MethodCoverage m3b = new MethodCoverage(duplicatedMethod, Arrays.asList(1, 3, 4, 5));

		tc.addMethod(m1);
		tc.addMethod(m2);
		tc.addMethod(m3);
		tc.addMethod(m3b);

		Set<MethodCoverage> expectedResult = new HashSet<>(Arrays.asList(m1, m2, m3));

		assertEquals(3, tc.getMethodCoverage().size());
		assertEquals(expectedResult, tc.getMethodCoverage());

		for (MethodCoverage mc : tc.getMethodCoverage()) {
			if (mc.getMethodName().equals(duplicatedMethod)) {
				assertEquals(6, mc.getCoveredLines().size());
			}
		}
	}

}