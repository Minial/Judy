package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JID5Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);
		assertMethodResultEquals(1);
		assertMutatedMethodResultEquals(0, 0);
	}

	@Mutate(operator = JID.class)
	public class TestClass {
		int i = 0;

		float f = 0;
		Integer o = null;
		long l = 0;
		long v = 1;
		double d = 0;

		public int test() {
			return (int) (i + f + l + d + v + (o == null ? 0 : 0));
		}
	}
}
