package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JID3Test extends AbstractMutationOperatorTesting {

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
			this(Math.max(10, 30));
			d = 300;
		}

		public TestClass(int i) {
		}

		int b = 2;
		int c = Math.max(1, 3);

		public TestClass(String s) {
			a += 100;
			b += 100;
			c += 100;
		}

		int d;

		public int test() {
			return a + b + c;
		}
	}
}
