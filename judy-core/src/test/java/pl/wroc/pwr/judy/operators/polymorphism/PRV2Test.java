package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import java.util.Map;

public class PRV2Test extends AbstractMutationOperatorTesting {
	@Override
	protected void initClassHierarchy(Map<String, String> inheritance) {
		inheritance.put("java/lang/String", "java/lang/Object");
	}

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);

		assertMethodResultEquals("VARIABLE");
		assertMutatedMethodResultEquals("FIELD", 0);
	}

	@Mutate(operator = PRV.class)
	public class TestClass {
		Object o = "FIELD";

		public String test() {
			Object x = "VARIABLE";
			Object obj = x;
			return obj.toString();
		}
	}
}
