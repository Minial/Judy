package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class OAC1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(4);

		assertMethodResultEquals(1);

		assertMutatedMethodResultEquals(-1, 0); // push(1, 2);
		assertMutatedMethodResultEquals(2, 1); // push(2);
		assertMutatedMethodResultEquals(1, 2); // push(1);
		assertMutatedMethodResultEquals(0, 3); // push();
	}

	@Mutate(operator = OAC.class)
	public class TestClass {
		public int test() {
			return push(2, "TEST", 1);
		}

		public int push(int a, String s, int b) {
			return a - b;
		}

		protected int push(int a) {
			return a;
		}

		@SuppressWarnings("unused")
		private int push() {
			return 0;
		}
	}

}
