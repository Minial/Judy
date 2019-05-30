package pl.wroc.pwr.judy.operators.javaspec;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import java.io.Serializable;

public class EOA3Test extends AbstractMutationOperatorTesting {
	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(4);

		assertMethodResultEquals("I didn't expect the Spanish Inquisition!", "test1");
		assertMethodResultEquals("I didn't expect the Spanish Inquisition!", "test2");
		assertMethodResultEquals("I didn't expect the Spanish Inquisition!", "test3");
		assertMethodResultEquals("Spanish Inquisition", "test4");

		assertMutatedMethodResultEquals("Spanish Inquisition", 0, "test1");
		assertMutatedMethodResultEquals("Spanish Inquisition", 1, "test2");
		assertMutatedMethodResultEquals("Spanish Inquisition", 2, "test3");
		assertMutatedMethodResultEquals("I didn't expect the Spanish Inquisition!", 3, "test4");

	}

	@Mutate(operator = EOA.class)
	public class TestClass {
		public String test1() {
			// super class
			EOABaseClass object1 = null;
			EOACloneable object2 = new EOACloneable();
			object2.content = "I didn't expect the Spanish Inquisition!";
			object1 = object2;
			return ((EOACloneable) object1).content;
		}

		public String test2() {
			// interface
			Cloneable object1 = null;
			EOACloneable object2 = new EOACloneable();
			object2.content = "I didn't expect the Spanish Inquisition!";
			object1 = object2;
			return ((EOACloneable) object1).content;
		}

		public String test3() {
			// interface of super class
			Serializable object1 = null;
			EOACloneable object2 = new EOACloneable();
			object2.content = "I didn't expect the Spanish Inquisition!";
			object1 = object2;
			return ((EOACloneable) object1).content;
		}

		public String test4() throws CloneNotSupportedException {
			Serializable object1 = null;
			EOACloneable object2 = new EOACloneable();
			object2.content = "I didn't expect the Spanish Inquisition!";
			object1 = (Serializable) object2.clone();
			return ((EOACloneable) object1).content;
		}
	}

	public class EOACloneable extends EOABaseClass implements Cloneable {
		private static final long serialVersionUID = 2L;

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

	public class EOABaseClass implements Serializable {
		private static final long serialVersionUID = 1L;
	}
}
