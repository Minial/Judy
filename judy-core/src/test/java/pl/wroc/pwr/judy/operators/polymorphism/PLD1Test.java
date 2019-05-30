package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class PLD1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);

		assertMethodResultEquals(610);

		assertMutatedMethodResultEquals(412, 0);
	}

	@Mutate(operator = PLD.class)
	public class TestClass {

		public int test() {
			return fun(new PLDChild(), 10);
		}

		int fun(PLDChild p, int i) {
			PLDChild l = new PLDChild();
			p.a = p.method() + p.a + i + l.method() + l.a;
			return p.a;
		}
	}

	public class PLDParent {

		public int a = 2;

		public int method() {
			return 1;
		}

	}

	public class PLDChild extends PLDParent implements Cloneable {

		public int a = 200;

		@Override
		public int method() {
			return 100;
		}

	}

}
