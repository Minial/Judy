package pl.wroc.pwr.judy.operators.inheritance;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class IPC2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);

		assertMethodResultEquals(25);

		assertMutatedMethodResultEquals(15, 0);
		assertMutatedMethodResultEquals(25, 1);
	}

	@Mutate(operator = IPC.class)
	public class TestClass extends IPCBaseClass {
		public int test() {
			return a;
		}

		public TestClass() {
			super(20);
			a += 5;
		}

		public TestClass(int b) {
		}

		public TestClass(int a, int b) {
			super(40);
			a += 5;
		}
	}

	public class IPCBaseClass {
		public IPCBaseClass() {
			a = 10;
		}

		public IPCBaseClass(int a) {
			this.a = a;
		}

		protected int a;
	}
}
