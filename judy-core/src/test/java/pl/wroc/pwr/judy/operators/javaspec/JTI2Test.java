package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JTI2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(2);

		assertMutatedMethodResultEquals(12, 0);
		assertMutatedMethodResultEquals(20, 1);
	}

	@Mutate(operator = JTI.class)
	public class TestClass {
		int a;

		long method(int a, int c, long b) {
			a = 10;
			b = 20;
			return this.a + this.b;
		}

		long b = 2;
		int c;

		public int test() {
			return (int) method(10, 999, 20);
		}
	}
}
