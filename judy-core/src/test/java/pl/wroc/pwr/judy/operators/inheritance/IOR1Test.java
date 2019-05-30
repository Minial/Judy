package pl.wroc.pwr.judy.operators.inheritance;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import java.util.Map;

public class IOR1Test extends AbstractMutationOperatorTesting {

	@Override
	protected void initClassHierarchy(Map<String, String> inheritance) {
		inheritance.put("pl/wroc/pwr/judy/operators/inheritance/IOR1Test$IORChildClass",
				"pl/wroc/pwr/judy/operators/inheritance/IOR1Test$TestClass");
	}

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);

		assertMethodResultEquals(100);

		assertMutatedMethodResultEquals(0, 0);
	}

	@Mutate(operator = IOR.class)
	public class TestClass {
		public int test() {
			TestClass tc = new IORChildClass();
			return tc.fun();
		}

		public int inner() {
			return 0;
		}

		public int fun() {
			return inner();
		}
	}

	public class IORChildClass extends TestClass {
		@Override
		public int inner() {
			return 100;
		}

		public int other() {
			return inner();
		}
	}
}
