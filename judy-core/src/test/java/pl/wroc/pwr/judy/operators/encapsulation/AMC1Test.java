package pl.wroc.pwr.judy.operators.encapsulation;

import org.junit.Ignore;
import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import pl.wroc.pwr.judy.utils.Accesses;

import java.util.Arrays;

import static org.junit.Assert.fail;

public class AMC1Test extends AbstractMutationOperatorTesting {

	@Ignore("AMC implementation has temporary changed.")
	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(12);

		assertMethodResultEquals(list(Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, Accesses.ACC_PRIVATE, 0));

		assertMutatedMethodResultEquals(
				list(Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, Accesses.ACC_PRIVATE, Accesses.ACC_PRIVATE), 0);
		assertMutatedMethodResultEquals(
				list(Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, Accesses.ACC_PRIVATE, Accesses.ACC_PROTECTED), 1);
		assertMutatedMethodResultEquals(
				list(Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, Accesses.ACC_PRIVATE, Accesses.ACC_PUBLIC), 2);

		assertMutatedMethodResultEquals(list(Accesses.ACC_PRIVATE, Accesses.ACC_PUBLIC, Accesses.ACC_PRIVATE, 0), 3);
		assertMutatedMethodResultEquals(list(Accesses.ACC_PROTECTED, Accesses.ACC_PUBLIC, Accesses.ACC_PRIVATE, 0), 4);
		assertMutatedMethodResultEquals(list(0, Accesses.ACC_PUBLIC, Accesses.ACC_PRIVATE, 0), 5);

		assertMutatedMethodResultEquals(list(Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, Accesses.ACC_PROTECTED, 0), 6);
		assertMutatedMethodResultEquals(list(Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, 0), 7);
		assertMutatedMethodResultEquals(list(Accesses.ACC_PUBLIC, Accesses.ACC_PUBLIC, 0, 0), 8);

		try {
			assertMutatedMethodResultEquals(0, 9);
			fail();
		} catch (Exception e) {
		}

		try {
			assertMutatedMethodResultEquals(0, 10);
			fail();
		} catch (Exception e) {
		}

		try {
			assertMutatedMethodResultEquals(0, 11);
			fail();
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("unused")
	@Mutate(operator = AMC.class)
	public class TestClass {
		public int pub() {
			return 0;
		}

		private int priv() {
			return 0;
		}

		public String test() {
			return list(TestClass.class.getDeclaredMethods()[0].getModifiers(),
					TestClass.class.getDeclaredMethods()[1].getModifiers(),
					TestClass.class.getDeclaredMethods()[2].getModifiers(),
					TestClass.class.getDeclaredFields()[0].getModifiers());
		}

		int a;
	}

	public String list(int... args) {
		Arrays.sort(args);
		StringBuilder builder = new StringBuilder();
		for (int i : args) {
			builder.append(i);
		}
		return builder.toString();
	}
}
