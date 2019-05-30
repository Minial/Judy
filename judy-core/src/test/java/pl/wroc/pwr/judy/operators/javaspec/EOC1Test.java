package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class EOC1Test extends AbstractMutationOperatorTesting {
	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(4);

		assertMethodResultEquals("B");
		assertMethodResultEquals(false, "test1");
		assertMethodResultEquals(true, "test2");

		assertMutatedMethodResultEquals("A", 0);
		assertMutatedMethodResultEquals("Empty", 1);
		assertMutatedMethodResultEquals(true, 2, "test1");
		assertMutatedMethodResultEquals(false, 3, "test2");
	}

	@Mutate(operator = EOC.class)
	public class TestClass {
		String s = "Empty";
		Integer i = new Integer(7);

		public String test() {
			Integer j = new Integer(7);
			if (i == j) {
				s = "A";
			} else if (i.equals(j)) {
				s = "B";
			}
			return s;
		}

		public boolean test1() {
			return i == new Integer(7);
		}

		public boolean test2() {
			return i.equals(new Integer(7));
		}
	}
}
