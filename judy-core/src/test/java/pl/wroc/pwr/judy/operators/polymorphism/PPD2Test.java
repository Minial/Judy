package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import static org.junit.Assert.fail;

public class PPD2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);

		assertMethodResultEquals(510);

		try {
			assertMutatedMethodResultEquals(NA, 0);
			fail();
		} catch (Exception e) {
		}
	}

	@Mutate(operator = PPD.class)
	public class TestClass {

		public int test() {
			return fun(new PPDChild());
		}

		int fun(PPDChild p) {
			PPDChild l = new PPDChild();
			p.a = p.method() + p.a + p.newMethod(10) + l.a;
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

		public int newMethod(int a) {
			return a;
		}

	}

}
