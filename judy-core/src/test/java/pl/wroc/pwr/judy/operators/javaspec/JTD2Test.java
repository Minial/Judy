package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JTD2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(30);

		assertMutatedMethodResultEquals(120, 0);
		assertMutatedMethodResultEquals(210, 1);
	}

	@Mutate(operator = JTD.class)
	public class TestClass {
		int a;

		long method(int a, int c, long b) {
			this.a = 100;
			this.b = 200;
			return a + b;
		}

		long b = 2;
		int c;

		public int test() {
			return (int) method(10, 999, 20);
		}
	}
}
