package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class EOC2Test extends AbstractMutationOperatorTesting {
	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(4);

		assertMethodResultEquals("A");
		assertMethodResultEquals(true, "test1");
		assertMethodResultEquals(false, "test2");

		assertMutatedMethodResultEquals("B", 0);
		assertMutatedMethodResultEquals("Empty", 1);
		assertMutatedMethodResultEquals(false, 2, "test1");
		assertMutatedMethodResultEquals(true, 3, "test2");
	}

	@Mutate(operator = EOC.class)
	public class TestClass {
		String s = "Empty";
		Integer i = new Integer(7);

		public String test() {
			Integer j = new Integer(7);
			if (!i.equals(j)) {
				s = "B";
			} else if (i != j) {
				s = "A";
			}
			return s;
		}

		public boolean test1() {
			return i != new Integer(7);
		}

		public boolean test2() {
			return !i.equals(new Integer(7));
		}
	}
}
