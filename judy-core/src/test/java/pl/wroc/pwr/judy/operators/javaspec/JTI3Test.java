package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class JTI3Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(2);
	}

	@Mutate(operator = JTI.class)
	public class TestClass<L, R> {

		public final L left;

		public final R right;

		public TestClass(L left, R right) {
			this.left = left;
			this.right = right;
		}
	}

}
