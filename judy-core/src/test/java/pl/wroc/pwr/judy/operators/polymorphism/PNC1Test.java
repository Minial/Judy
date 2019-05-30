package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.PNCParent;

import java.util.Map;

public class PNC1Test extends AbstractMutationOperatorTesting {

	@Override
	protected void initClassHierarchy(Map<String, String> inheritance) {
		inheritance.put("resources/operators/PNCChild1", "resources/operators/PNCParent");
		inheritance.put("resources/operators/PNCChild2", "resources/operators/PNCParent");
	}

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(2);
		assertMutantsCountEquals(4);

		assertMethodResultEquals(15);

		assertMutatedMethodResultEquals(25, 0);
		assertMutatedMethodResultEquals(35, 1);
		assertMutatedMethodResultEquals(25, 2);
		assertMutatedMethodResultEquals(35, 3);
	}

	@Mutate(operator = PNC.class)
	public class TestClass {

		public int test() {
			PNCParent p1 = new PNCParent();
			PNCParent p2 = new PNCParent();
			return p1.fun(5) + p2.fun(10);
		}

	}

}
