package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

public class OAC2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		// assertMutationPointsCountEquals(1); // --> 19
		// assertMutantsCountEquals(14); // --> 19

		assertMethodResultEquals("100startend200");

		assertMutatedMethodResultEquals("200startend100", 0);
		assertMutatedMethodResultEquals("100endstart200", 1);
		assertMutatedMethodResultEquals("200endstart100", 2);
		assertMutatedMethodResultEquals("100", 3);
		assertMutatedMethodResultEquals("200", 4);
		assertMutatedMethodResultEquals("start", 5);
		assertMutatedMethodResultEquals("end", 6);
		assertMutatedMethodResultEquals("startend", 7);
		assertMutatedMethodResultEquals("endstart", 8);
		assertMutatedMethodResultEquals("start100", 9);
		assertMutatedMethodResultEquals("start200", 10);
		assertMutatedMethodResultEquals("end100", 11);
		assertMutatedMethodResultEquals("end200", 12);
		assertMutatedMethodResultEquals("EMPTY", 13);
	}

	@SuppressWarnings("unused")
	@Mutate(operator = OAC.class)
	public class TestClass {
		public String test() {
			return push(100, "start", "end", 200);
		}

		public String push(int a, String s1, String s2, int b) {
			return a + s1 + s2 + b;
		}

		protected String push(int a) {
			return Integer.toString(a);
		}

		protected String push(String s) {
			return s;
		}

		protected String push(String s1, String s2) {
			return s1 + s2;
		}

		public String push(String s, int a) {
			return s + a;
		}

		private String push() {
			return "EMPTY";
		}

		public String push(int a, String s1, String s2, int b, String c) {
			return a + s1 + s2 + b + c;
		}

		private int push(int a, int b) {
			return a - b;
		}
	}
}
