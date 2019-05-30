package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JDC1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);

		assertMethodResultEquals(3);

		assertMutatedMethodResultEquals(0, 0);
	}

	@Mutate(operator = JDC.class)
	public class TestClass {
		int a;

		public TestClass() {

			a = 1;
			b = 2;
		}

		int b;
		int c;

		public int test() {
			return a + b;
		}
	}
}
