package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JDC2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(0);
		assertMutantsCountEquals(0);

		assertMethodResultEquals(10);
	}

	@Mutate(operator = JDC.class)
	public class TestClass {
		int a = 10;

		public TestClass() {
		}

		int b;
		int c;

		public int test() {
			return a + b;
		}
	}
}
