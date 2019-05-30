package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import java.io.Serializable;
import java.util.Map;

public class PRV1Test extends AbstractMutationOperatorTesting {
	@Override
	protected void initClassHierarchy(Map<String, String> inheritance) {
		inheritance.put("java/lang/String", "java/lang/Object");
		inheritance.put("java/lang/Integer", "java/lang/Number");
		inheritance.put("java/lang/Number", "java/lang/Object");
		inheritance.put("java/io/Serializable", "java/lang/Object");
	}

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(5);

		assertMethodResultEquals("ORIGINAL");

		assertMutatedMethodResultEquals("5", 0);
		assertMutatedMethodResultEquals("10", 1);
		assertMutatedMethodResultEquals("NEW", 2);
		assertMutatedMethodResultEquals("1.5", 3);
		assertMutatedMethodResultEquals("0.6", 4);
	}

	@Mutate(operator = PRV.class)
	public class TestClass {
		Object o = "ORIGINAL";
		Integer a = 5;
		Integer b = 10;
		String s = "NEW";

		@SuppressWarnings("unused")
		public String test() {
			Number a = 1.5;
			Serializable s = 0.6f;
			Object obj = o;
			return obj.toString();
		}
	}
}
