package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JTD1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(6);

		assertMutatedMethodResultEquals(16, 0);
		assertMutatedMethodResultEquals(24, 1);
	}

	@Mutate(operator = JTD.class)
	public class TestClass {
		long a;

		long method(long a, int c, int b) {
			return this.a + this.b + 4;
		}

		int b = 2;
		int c;

		public int test() {
			return (int) method(10L, 999, 20);
		}
	}
}
