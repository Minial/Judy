package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class OMR1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(3);
		assertMutantsCountEquals(6);

		assertMethodResultEquals(1);

		assertMutatedMethodResultEquals(2, 0); // push(2,"Test",1) -> push(2);
		assertMutatedMethodResultEquals(1, 1); // push(2,"Test",1) -> push(1);
		assertMutatedMethodResultEquals(6, 2); // push(2,"Test",1) -> push();
		assertMutatedMethodResultEquals(7, 3); // push(2,"Test",1) ->
		// push("Test");
		assertMutatedMethodResultEquals(1, 4); // push(a) -> push(); mutant has
		// no impact
		assertMutatedMethodResultEquals(1, 5); // push(s) -> push(); mutant has
		// no impact
	}

	@Mutate(operator = OMR.class)
	public class TestClass {
		public int test() {
			return push(2, "Test", 1);
		}

		public int push(int a, String s, int b) {
			return a - b;
		}

		protected int push(int a) {
			return a;
		}

		protected int push() {
			return 6;
		}

		protected int push(String s) {
			return 7;
		}
	}
}
