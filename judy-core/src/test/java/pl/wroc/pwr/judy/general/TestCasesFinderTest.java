package pl.wroc.pwr.judy.general;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCasesFinderTest {
	@Test
	public void shouldFindJUnit3Testcases() throws Exception {
		Set<String> result = TestCasesFinder.findTestMethods(JUnit3ExampleTestClass.class);
		assertEquals(2, result.size());
		assertTrue(result.contains("testCaseOk"));
		assertTrue(result.contains("testCaseOk2"));
	}

	@Test
	public void shouldFindJUnit4Testcases() throws Exception {
		Set<String> result = TestCasesFinder.findTestMethods(JUnit4ExampleTestClass.class);
		assertEquals(2, result.size());
		assertTrue(result.contains("shouldBeOk"));
		assertTrue(result.contains("testCaseOk2"));
	}
}
