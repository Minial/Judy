package pl.wroc.pwr.judy.operators.other;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class RemoveCalls1Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(10);
		assertMutantsCountEquals(10);

		assertMethodResultEquals(52);

		assertMutatedMethodResultEquals(47, 0);
		assertMutatedMethodResultEquals(57, 1);
		assertMutatedMethodResultEquals(36, 2);
		assertMutatedMethodResultEquals(22, 3);
		assertMutatedMethodResultEquals(72, 4);
		assertMutatedMethodResultEquals(42, 5);
		assertMutatedMethodResultEquals(42, 6);
		try {
			assertMutatedMethodResultEquals(52, 7);
			fail();
		} catch (Exception e) {
		}

		assertMutatedMethodResultEquals(52, 8);

		try {
			assertMutatedMethodResultEquals(46, 9);
			fail();
		} catch (Exception e) {
		}
	}

	@Mutate(operator = RemoveCalls.class)
	public class TestClass extends BaseClass {

		@SuppressWarnings("cast")
		public int test() {
			add5();
			sub5();
			counter += add16();
			add(10, 10, 10);
			counter += sub(10, 10);
			add(10);
			counter += add10();

			counter += (Integer) 0; // must be: counter += (Integer) 0;
			int[] t = add(new ArrayList<Integer>());
			for (int element : t) {
				counter += element;
			}
			return counter;
		}

		public void add5() {
			counter += 5;
		}

		public void sub5() {
			counter -= 5;
		}

		private int add16() {
			return 16;
		}

		private void add(int... args) {
			for (int i : args) {
				counter += i;
			}
		}

		private int sub(int i, int j) {
			return -i - j;
		}

		public int[] add(List<Integer> list) {
			int[] result = new int[3];
			result[0] = 1;
			result[1] = 2;
			result[2] = 3;
			return result;
		}

		int counter = 0;
	}

	public static class BaseClass {
		protected static int add10() {
			return 10;
		}
	}

}
