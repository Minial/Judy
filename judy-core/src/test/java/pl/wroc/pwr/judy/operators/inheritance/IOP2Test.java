package pl.wroc.pwr.judy.operators.inheritance;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class IOP2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(5);

		assertMethodResultEquals(50 + 25 + 30 + 10);

		assertMutatedMethodResultEquals(25 + 25 + 30 + 10, 0);
		assertMutatedMethodResultEquals(50 + 50 + 30 + 10, 1);
		assertMutatedMethodResultEquals(50 + 25 + 60 + 10, 2);
		assertMutatedMethodResultEquals(50 + 25 + 25 + 10, 3);
		assertMutatedMethodResultEquals(50 + 25 + 50 + 10, 4);
	}

	@Mutate(operator = IOP.class)
	public class TestClass extends IOPBaseClass {
		public int test() {
			fun1(2);
			fun2(2, 1, "Test");
			fun3();
			return a + b + c + fun4();
		}

		@Override
		protected void fun1(int x) {
			a = 5;
			a += 20;
			super.fun1(x);
		}

		@Override
		protected void fun2(int x, int w, String s) {
			super.fun2(x, w, s);
			b = 5;
			b += 20;
		}

		@Override
		protected void fun3() {
			super.fun3();
			c = 5;
			super.fun3();
			c += 20;
		}

		@Override
		protected int fun4() {
			d = 5;
			d = super.fun4();
			return d;
		}
	}

	public class IOPBaseClass {
		protected void fun1(int x) {
			a *= x;
		}

		protected void fun2(int x, int w, String s) {
			s.charAt(w);
			b *= x;
		}

		protected void fun3() {
			c *= 2;
		}

		protected int fun4() {
			return 10;
		}

		protected int a;
		protected int b;
		protected int c;
		protected int d;
	}
}
