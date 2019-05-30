package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JID6Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);
		assertMethodResultEquals(11);
		assertMutatedMethodResultEquals(10, 0);
	}

	@Mutate(operator = JID.class)
	public class TestClass {
		int i = 0;

		public TestClass() {
			i = 10;
		}

		float f = 0;
		Integer o = null;
		long l = 0;
		long v = 1;
		double d = 0;

		public TestClass(int b) {
			i = b;
		}

		public int test() {
			return (int) (i + f + l + d + v + (o == null ? 0 : 0));
		}
	}
}
