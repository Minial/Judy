package pl.wroc.pwr.judy.operators.inheritance;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.inheritance.ISIBaseClass;

@SuppressWarnings("unused")
public class ISD1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(4);

		assertMethodResultEquals(105);

		assertMutatedMethodResultEquals(123, 0);
		assertMutatedMethodResultEquals(132, 1);

		assertMethodResultEquals(105, "testMethods");

		assertMutatedMethodResultEquals(123, 2, "testMethods");
		assertMutatedMethodResultEquals(132, 3, "testMethods");
	}

	@Mutate(operator = ISD.class)
	public class TestClass extends ISIBaseClass {
		public int test() {
			return super.pub + super.prot + super.inherited;
		}

		public int testMethods() {
			return super.pub() + super.prot() + super.inherited();
		}

		int i(int num) {
			// to prevent compiler from pushing precomputed values
			return num;
		}

		private final int def = i(10);
		private final int pub = i(20);
		private final int prot = i(30);
		private final int priv = i(40);

		public int def() {
			return 9;
		}

		@Override
		public int pub() {
			return 20;
		}

		@Override
		public int prot() {
			return 30;
		}

		public int priv() {
			return 40;
		}
	}
}
