package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class EAM1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(4);

		assertMethodResultEquals(3);

		assertMutatedMethodResultEquals(4, 0);
		assertMutatedMethodResultEquals(6, 1);
		assertMutatedMethodResultEquals(2, 2);
		assertMutatedMethodResultEquals(5, 3);
	}

	@Mutate(operator = EAM.class)
	public class TestClass {
		public int test() {
			return getX() + getY();
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}

		public String getQ() {
			return q;
		}

		public int method() {
			return 100;
		}

		private final int x = 1;
		private final int y = 2;
		private final int z = 4;
		private final String q = "4";
	}
}
