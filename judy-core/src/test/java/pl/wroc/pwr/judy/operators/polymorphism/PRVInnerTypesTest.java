package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import java.util.Map;

public class PRVInnerTypesTest extends AbstractMutationOperatorTesting {
	@Override
	protected void initClassHierarchy(Map<String, String> inheritance) {
		inheritance.put("pl/wroc/pwr/judy/operators/polymorphism/PRVInnerTypesTest$B",
				"pl/wroc/pwr/judy/operators/polymorphism/PRVInnerTypesTest$A");
	}

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(1);
		assertMutantsCountEquals(1);

		assertMethodResultEquals("A");
		assertMutatedMethodResultEquals("B", 0);
	}

	@Mutate(operator = PRV.class)
	public class TestClass {

		A a = new A();
		B b = new B();

		public String test() {
			A aa = a;
			return aa.x();
		}
	}

	private class A {
		public String x() {
			return "A";
		}
	}

	private class B extends A {
		@Override
		public String x() {
			return "B";
		}
	}
}
