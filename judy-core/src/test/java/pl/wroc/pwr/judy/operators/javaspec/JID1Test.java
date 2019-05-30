package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JID1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(3);
		assertMutantsCountEquals(3);

		assertMethodResultEquals(6);

		assertMutatedMethodResultEquals(5, 0);
		assertMutatedMethodResultEquals(4, 1);
		assertMutatedMethodResultEquals(3, 2);
	}

	@Mutate(operator = JID.class)
	public class TestClass {
		int a = 1;

		public TestClass() {
		}

		double b = 2;
		int c = Math.max(1, 3);

		public int test() {
			return (int) (a + b + c);
		}
	}
}
