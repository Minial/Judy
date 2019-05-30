package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MethodCoverageTest {
	private static final String METHOD_NAME = "methodName";
	private MethodCoverage mc;

	@Before
	public void setUp() {
		mc = new MethodCoverage(METHOD_NAME);
	}

	@Test
	public void shouldReturnMethodName() throws Exception {
		assertEquals(METHOD_NAME, mc.getMethodName());
	}

	@Test
	public void shouldAddCoveredLinesOneByOne() throws Exception {
		assertTrue(mc.getCoveredLines().isEmpty());

		mc.addLine(1);
		mc.addLine(2);
		mc.addLine(3);
		mc.addLine(3);

		checkAddedLines();
	}

	@Test
	public void shouldAddMultipleCoveredLinesAtOnce() throws Exception {
		assertTrue(mc.getCoveredLines().isEmpty());
		mc.addLines(Arrays.asList(1, 2, 3, 3));
		checkAddedLines();
	}

	public void checkAddedLines() {
		List<Integer> expectedResult = Arrays.asList(1, 2, 3, 3);
		assertEquals(4, mc.getCoveredLines().size());
		assertEquals(expectedResult, mc.getCoveredLines());
	}

}
