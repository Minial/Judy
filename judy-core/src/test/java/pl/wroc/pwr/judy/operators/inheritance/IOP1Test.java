package pl.wroc.pwr.judy.operators.inheritance;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class IOP1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(30);

		assertMutatedMethodResultEquals(25, 0);
		assertMutatedMethodResultEquals(50, 1);
	}

	@Mutate(operator = IOP.class)
	public class TestClass extends IOPBaseClass {
		public int test() {
			fun(2);
			return a;
		}

		@Override
		protected void fun(int x) {
			a = 5;
			super.fun(x);
			a += 20;
		}
	}

	public class IOPBaseClass {
		protected void fun(int x) {
			a *= x;
		}

		protected int a;
	}
}
