package pl.wroc.pwr.judy.operators.inheritance;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.inheritance.ISIBaseClass;

public class ISI1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(6);
		assertMutantsCountEquals(6);

		assertMethodResultEquals(210);

		assertMutatedMethodResultEquals(190, 0); // PUTFIELD pub = 20 ->
		// PUTFIELD super.pub = 20
		assertMutatedMethodResultEquals(180, 1); // PUTFIELD prot = 30 ->
		// PUTFIELD super.prot = 30
		assertMutatedMethodResultEquals(192, 2);
		assertMutatedMethodResultEquals(183, 3);

		assertMethodResultEquals(219, "testMethods");

		assertMutatedMethodResultEquals(201, 4, "testMethods");
		assertMutatedMethodResultEquals(192, 5, "testMethods");
	}

	@Mutate(operator = ISI.class)
	public class TestClass extends ISIBaseClass {
		public int test() {
			return def + pub + prot + priv + inherited + new ISIChild().other;
		}

		public int testMethods() {
			return def() + pub() + prot() + priv() + inherited() + new ISIChild().other();
		}

		int i(int num) {
			// to prevent compiler from pushing precomputed values
			return num;
		}

		private final int def = i(10);
		private final int pub = i(20);
		private final int prot = i(30);
		private final int priv = i(40);

		public int def() {
			return 9;
		}

		@Override
		public int pub() {
			return 20;
		}

		@Override
		public int prot() {
			return 30;
		}

		public int priv() {
			return 40;
		}
	}

	public class ISIParent {
		public int other = 1;

		public int other() {
			return 2;
		}
	}

	public class ISIChild extends ISIParent {
		public int other = 10;

		@Override
		public int other() {
			return 20;
		}
	}

}