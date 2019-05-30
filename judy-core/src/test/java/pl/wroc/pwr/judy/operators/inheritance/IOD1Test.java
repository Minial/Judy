package pl.wroc.pwr.judy.operators.inheritance;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class IOD1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(-2);

		assertMutatedMethodResultEquals(8, 0);
		assertMutatedMethodResultEquals(22, 1);
	}

	@Mutate(operator = IOD.class)
	public class TestClass extends IODBaseClass {
		public int test() {
			fun1(10);
			fun2(6, 6);
			return a;
		}

		@Override
		protected void fun1(int val) {
			a = val;
		}

		@Override
		protected void fun2(int val1, int val2) {
			a -= val1 + val2;
		}
	}

	public class IODBaseClass {

		protected void fun1(int val) {
			a = 2 * val;
		}

		protected void fun2(int val1, int val2) {
			a += val1 + val2;
		}

		protected int a;
	}
}
