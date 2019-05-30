package pl.wroc.pwr.judy.general;

import junit.framework.TestCase;
import org.junit.Test;

public class JUnit4ExampleTestClass extends TestCase {

	@Test
	public void shouldBeOk() throws Exception {
	}

	public void invalidTestCase() {

	}

	public void testCaseOk2() {
		teStCaseInvalid2();
	}

	@Test
	private void teStCaseInvalid2() {
	}

	public String testCaseInvalid3() {
		teStCaseInvalid2();
		return null;
	}

	@Test
	public void testCaseInvalid4(String x) {

	}

}
