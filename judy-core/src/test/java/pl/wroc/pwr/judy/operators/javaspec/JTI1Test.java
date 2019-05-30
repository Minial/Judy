package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JTI1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(34);

		assertMutatedMethodResultEquals(24, 0);
		assertMutatedMethodResultEquals(16, 1);
	}

	@Mutate(operator = JTI.class)
	public class TestClass {
		long a;

		long method(long a, int c, int b) {
			return a + b + 4;
		}

		int b = 2;
		int c;

		public int test() {
			return (int) method(10L, 999, 20);
		}
	}
}
