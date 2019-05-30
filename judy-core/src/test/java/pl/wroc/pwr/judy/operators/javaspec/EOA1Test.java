package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class EOA1Test extends AbstractMutationOperatorTesting {
	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(4);

		assertMethodResultEquals("I didn't expect the Spanish Inquisition!", "test1");
		assertMethodResultEquals("I didn't expect the Spanish Inquisition!", "test2");
		assertMethodResultEquals("I didn't expect the Spanish Inquisition!", "test3");
		assertMethodResultEquals("I didn't expect the Spanish Inquisition!", "test4");

		assertMutatedMethodResultEquals("Spanish Inquisition", 0, "test1");
		assertMutatedMethodResultEquals("Spanish Inquisition", 1, "test2");
		assertMutatedMethodResultEquals("Spanish Inquisition", 2, "test3");
		assertMutatedMethodResultEquals("Spanish Inquisition", 3, "test4");
	}

	@Mutate(operator = EOA.class)
	public class TestClass {
		public String test1() {
			// local variable x2
			EOACloneable object1 = null;
			EOACloneable object2 = new EOACloneable();
			object2.content = "I didn't expect the Spanish Inquisition!";
			object1 = object2;
			return object1.content;
		}

		public String test2() {
			// field x2
			field1 = null;
			field2 = new EOACloneable();
			field2.content = "I didn't expect the Spanish Inquisition!";
			field1 = field2;
			return field1.content;
		}

		public String test3() {
			// field to local variable
			EOACloneable object1 = null;
			field2 = new EOACloneable();
			field2.content = "I didn't expect the Spanish Inquisition!";
			object1 = field2;
			return object1.content;
		}

		public String test4() {
			// local variable to field
			EOACloneable object2 = new EOACloneable();
			object2.content = "I didn't expect the Spanish Inquisition!";
			field1 = object2;
			return field1.content;
		}

		EOACloneable field1;
		EOACloneable field2;

	}

	public class EOACloneable implements Cloneable {
		@Override
		public Object clone() throws CloneNotSupportedException {
			Object copy = super.clone();
			((EOACloneable) copy).content = "Spanish Inquisition"; // ha! nobody
			// expects
			// the
			// Spanish
			// Inquisition!
			return copy;
		}

		public String content;
	}
}
