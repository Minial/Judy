package pl.wroc.pwr.judy.operators.arithmetic;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class AIR_Add1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(24);

		assertMutatedMethodResultEquals(20, 0);
		assertMutatedMethodResultEquals(10, 1);
	}

	@Mutate(operator = AIR_Add.class)
	public class TestClass {
		public int test() {
			return i(2) * i(3) * i(4);
			// 0: (2 + 3) * 4 = 20
			// 1: (2 * 3) + 4 = 10
			// and not like in source mutations:
			// 0: 2 + (3 * 4) = 24
			// 1: (2 * 3) + 4 = 10
		}

		private int i(int num) {
			// to prevent compiler from pushing precomputed values
			return num;
		}
	}
}
