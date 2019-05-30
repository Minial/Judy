package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class OMD1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(4);

		assertMethodResultEquals(7);

		assertMutatedMethodResultEquals(6, 0);
		assertMutatedMethodResultEquals(6, 1);
		assertMutatedMethodResultEquals(6, 2);
		assertMutatedMethodResultEquals(6, 3);
	}

	@Mutate(operator = OMD.class)
	public class TestClass {
		public int test() {
			return this.getClass().getDeclaredMethods().length;
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

		protected String push(String s1, String s2) {
			return "TEST";
		}

		protected int pop(String s) {
			return 8;
		}
	}
}
