package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class PPD1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);

		assertMethodResultEquals(510);

		assertMutatedMethodResultEquals(312, 0);
	}

	@Mutate(operator = PPD.class)
	public class TestClass {

		public int test() {
			return fun(new PPDChild(), 10);
		}

		int fun(PPDChild p, int i) {
			PPDChild l = new PPDChild();
			p.a = p.method() + p.a + i + l.a;
			return p.a;
		}

	}

	public class PPDParent {

		public int a = 2;

		public int method() {
			return 1;
		}

	}

	public class PPDChild extends PPDParent implements Cloneable {

		public int a = 200;

		@Override
		public int method() {
			return 100;
		}

	}

}
