package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class EMM1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(4);

		assertMethodResultEquals(14);

		assertMutatedMethodResultEquals(5, 0);
		assertMutatedMethodResultEquals(11, 1);
		assertMutatedMethodResultEquals(9, 2);
		assertMutatedMethodResultEquals(15, 3);
	}

	@Mutate(operator = EMM.class)
	public class TestClass {
		public int test() {
			setX(10);
			setY(5);
			return x + z;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public void setZ(int z) {
			this.z = z;
		}

		public void setQ(String q) {
			this.q = q;
		}

		public void method(int a) {
			x += a;
			y += a;
			z += a;
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

		private int x = 1;
		private int y = 2;
		private int z = 4;
		private String q = "4";
	}
}
